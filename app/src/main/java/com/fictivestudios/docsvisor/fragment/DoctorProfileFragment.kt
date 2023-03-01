package com.fictivestudios.docsvisor.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.ilhasoft.support.validation.Validator
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.HomeActivity
import com.fictivestudios.docsvisor.adapter.DoctorPatientProfileAdapter
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.GetPatientResponse
import com.fictivestudios.docsvisor.apiManager.response.PatientData
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.constants.DOCTOR_BOTTOMNAV
import com.fictivestudios.docsvisor.databinding.Doctorprofilebind
import com.fictivestudios.docsvisor.helper.PreferenceUtils
import com.fictivestudios.docsvisor.helper.USER_OBJECT
import com.fictivestudios.docsvisor.model.User
import com.fictivestudios.docsvisor.widget.TitleBar
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.bottom_doctor_nav.*
import kotlinx.android.synthetic.main.fragment_doctor_profile.*
import kotlinx.android.synthetic.main.item_doctor_patientlist.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class DoctorProfileFragment : BaseFragment(), OnItemClickListener {

    lateinit var binding: Doctorprofilebind
    private var userObj :User?=null
    // private lateinit var viewModel: NearbySessionViewModel
    var validator: Validator? = null
    private var arrayList: List<PatientData>? = null
    private lateinit var nearbySessionAdapter: DoctorPatientProfileAdapter

    var patientId = ""
    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_doctor_profile,
                container,
                false
            )

/*
        binding.model = viewModel
        binding.lifecycleOwner = this*/

        getUser()

        return binding.root

        //  return inflater.inflate(R.layout.fragment_nearby_sessions, container, false);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*    viewModel = ViewModelProvider.AndroidViewModelFactory(Application())
                .create<NearbySessionViewModel>(NearbySessionViewModel::class.java)*/
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferenceManager?.putValue(
                DOCTOR_BOTTOMNAV,
                "Profile"
        )

        HomeActivity.home.doctorProfile()
        HomeActivity.home.showBottomBar()
        arrayList = ArrayList()

        val mLayoutManager2: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvRecentPatient.layoutManager = mLayoutManager2
        (rvRecentPatient.itemAnimator as DefaultItemAnimator).supportsChangeAnimations =
            false

        /*rvRecentPatient.setLayoutManager(
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false),
            R.layout.item_nearby_session_shimmer
        )
        rvRecentPatient.setItemViewType(ShimmerAdapter.ItemViewType { type: Int, position: Int -> R.layout.item_nearby_session_shimmer })
*/
        getPatientList()
    }

    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        //titleBar?.showBackButton(activity, "PROFILE")
        titleBar?.showSidebar(getBaseActivity(), "Doctor")
        titleBar?.setTitle("PROFILE")
    }

    override fun setListeners() {

    }

    companion object {

        fun newInstance(): DoctorProfileFragment {
            val args = Bundle()
            val fragment = DoctorProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }



    private fun getPatientList()
    {

        binding.pbGetpatient.visibility=View.VISIBLE

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.getPatient().enqueue(object: retrofit2.Callback<GetPatientResponse> {
                override fun onResponse(
                    call: Call<GetPatientResponse>,
                    response: Response<GetPatientResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbGetpatient.visibility=View.GONE


                    })

                    Log.d("Response", response.message())

                    try {


                        if (response.isSuccessful) {

                            if (response.body()?.status==1)
                            {
                                Log.d("Response", response.body()!!.message)

                                Log.d("Response", response.body()!!.data.toString())

                                Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()

                                arrayList = response.body()?.data

                                nearbySessionAdapter = DoctorPatientProfileAdapter(requireActivity(), arrayList!!, this@DoctorProfileFragment)

                                rvRecentPatient.adapter = nearbySessionAdapter
                                nearbySessionAdapter.notifyDataSetChanged()


                            }
                            else
                            {
                                Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                            }

                        }
                        else {
                            Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    catch (e:Exception)
                    {
                        //Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<GetPatientResponse>, t: Throwable)
                {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbGetpatient.visibility=View.GONE


                    })
                }
            })

        }


    }

    override fun onItemClick(position: Int, `object`: Any?, view: View?, adapterName: String?) {

    }

    private fun getUser()
    {

        var task = GlobalScope.async(Dispatchers.IO) {
            var gson =  Gson()
            var json:String= PreferenceUtils.getString(USER_OBJECT)
            userObj=gson.fromJson(json, User::class.java)

        }
        GlobalScope.launch(Dispatchers.Main) {
            task.await()
            setUser()
        }



    }
    private fun setUser() {


        if (userObj!=null)
        {
            binding.txtName.setText(userObj?.name)
            binding.txtEmail.setText(userObj?.email)
            if (!userObj?.image.isNullOrEmpty())
            {
                Picasso.get().load(userObj?.image).into(binding.img)
            }


        }


    }

    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            /*if (isLoading == null) return
            if (isLoading) {
                rvRecentPatient?.showShimmer()
            } else {
                rvRecentPatient?.hideShimmer()
            }*/
        }
    }

    /* internal inner class WebResponseObserver : Observer<WebResponse<Any>> {
         override fun onChanged(webResponse: WebResponse<Any>) {
             val type =
                 object : TypeToken<java.util.ArrayList<NearbyReceivingModel>?>() {}.type
             val arr: java.util.ArrayList<NearbyReceivingModel> = GsonFactory.getSimpleGson()
                 .fromJson(
                     GsonFactory.getSimpleGson()
                         .toJson(webResponse.result), type
                 )
             arrayList?.clear()
             arrayList?.addAll(arr)
             nearbySessionAdapter.notifyDataSetChanged()
         }
     }
 */
}