package com.fictivestudios.docsvisor.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.ilhasoft.support.validation.Validator
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.HomeActivity
import com.fictivestudios.docsvisor.activities.HomeActivityPateint
import com.fictivestudios.docsvisor.activities.MainActivity
import com.fictivestudios.docsvisor.adapter.DoctorPatientListAdapter
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.GetPatientResponse
import com.fictivestudios.docsvisor.apiManager.response.PatientData
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.constants.DOCTOR_BOTTOMNAV
import com.fictivestudios.docsvisor.databinding.FragmentDoctorPatientlistBinding
import com.fictivestudios.docsvisor.helper.ACCESS_TOKEN
import com.fictivestudios.docsvisor.helper.PreferenceUtils
import com.fictivestudios.docsvisor.helper.USER_OBJECT
import com.fictivestudios.docsvisor.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_doctor_list.*
import kotlinx.android.synthetic.main.fragment_doctor_patientlist.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class DoctorPatientFragment : BaseFragment(), OnItemClickListener {

    lateinit var binding: FragmentDoctorPatientlistBinding

    // private lateinit var viewModel: NearbySessionViewModel
    var validator: Validator? = null
    private var arrayList: List<PatientData>? = null
    private lateinit var adapter: DoctorPatientListAdapter


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
                R.layout.fragment_doctor_patientlist,
                container,
                false
            )
/*
        binding.model = viewModel
        binding.lifecycleOwner = this*/
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
            "Home"
        )
        val mLayoutManager2: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvPatients.layoutManager = mLayoutManager2
        (binding.rvPatients.itemAnimator as DefaultItemAnimator).supportsChangeAnimations =
            false


        HomeActivity.home.doctorPatientFragment()
        HomeActivity.home.showBottomBar()


        getPatientList()
    /*   arrayList = ArrayList()
        nearbySessionAdapter = DoctorPatientListAdapter(requireActivity(), arrayList!!, this)
        arrayList?.addAll(Constants.sampleList)*/






    }

    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE

        titleBar?.setBtnRight(R.drawable.notifications, View.OnClickListener {

        })
        titleBar?.showSidebar(getBaseActivity(), "Doctor")
        titleBar?.setTitle("HOME")
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
                        if (response.message() == "Unauthenticated." || response.message() == "Unauthorized")
                        {
                            PreferenceUtils.remove(USER_OBJECT)
                            PreferenceUtils.remove(ACCESS_TOKEN)
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            activity?.finish()
                        }

                        if (response.isSuccessful) {

                            if (response.body()?.status==1)
                            {
                                Log.d("Response", response.body()!!.message)

                                Log.d("Response", response.body()!!.data.toString())

                                Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                                arrayList = ArrayList()
                                arrayList = response.body()?.data
                                adapter = DoctorPatientListAdapter(requireActivity(), arrayList!!, this@DoctorPatientFragment)
                                binding.rvPatients.adapter = adapter
                                adapter.notifyDataSetChanged()
                            }
                            else
                            {  activity?.runOnUiThread(java.lang.Runnable {
                                Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()

                            })


                            }

                        }
                        else {
                            activity?.runOnUiThread(java.lang.Runnable {
                                Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()

                            })

                        }
                    }
                    catch (e:Exception)
                    { activity?.runOnUiThread(java.lang.Runnable {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()

                    })
                        //
                    }
                }

                override fun onFailure(call: Call<GetPatientResponse>, t: Throwable)
                {

                     activity?.runOnUiThread(java.lang.Runnable {
                         binding.pbGetpatient.visibility=View.GONE
                         Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()

                     })
                }
            })

        }


    }


    override fun setListeners() {
        symptoms.setOnClickListener {
            HomeActivity.navControllerHome.navigate(R.id.symptomFragment2)

        }

    }

    companion object {

        fun newInstance(): DoctorPatientFragment {

            val args = Bundle()

            val fragment = DoctorPatientFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onItemClick(position: Int, `object`: Any?, v: View?, adapterName: String?) {
        when (v!!.id) {
            R.id.menu -> {
                val popup = PopupMenu(requireContext(), v!!)
                popup.inflate(R.menu.menu_patient)
                popup.setOnMenuItemClickListener(object :
                    MenuItem.OnMenuItemClickListener,
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem): Boolean {
                        when (item.itemId) {
                            R.id.menuChat -> {

                                HomeActivity.navControllerHome.navigate(R.id.messengerFragment)

                            }
                        }
                        return false
                    }
                })
                popup.show()
            }
            R.id.materialCardView -> {
                HomeActivity.navControllerHome.navigate(R.id.doctorPatientProfile)

                val bundle = bundleOf("patientId" to arrayList?.get(position)?.id.toString())
                HomeActivity.navControllerHome.navigate(R.id.doctorPatientProfile,bundle)
            }
        }

    }


    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            if (isLoading == null) return
            /* if (isLoading) {
                 rvPatients?.showShimmer()
             } else {
                 rvPatients?.hideShimmer()
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