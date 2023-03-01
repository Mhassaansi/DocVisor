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
import com.fictivestudios.docsvisor.activities.HomeActivityPateint
import com.fictivestudios.docsvisor.adapter.DoctorPatientTestAdapter
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.Report
import com.fictivestudios.docsvisor.apiManager.response.TestHistoryResponse
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.callbacks.OnSpinnerOKPressedListener
import com.fictivestudios.docsvisor.constants.Constants
import com.fictivestudios.docsvisor.constants.DOCTOR_BOTTOMNAV
import com.fictivestudios.docsvisor.databinding.FragmentPatientProfileBinding
import com.fictivestudios.docsvisor.helper.PreferenceUtils
import com.fictivestudios.docsvisor.helper.USER_OBJECT
import com.fictivestudios.docsvisor.model.SpinnerModel
import com.fictivestudios.docsvisor.model.User
import com.fictivestudios.docsvisor.widget.TitleBar
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_patient_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class PatientProfileFragment : BaseFragment(), OnItemClickListener,
    OnChartValueSelectedListener {

    lateinit var binding: FragmentPatientProfileBinding
    var validator: Validator? = null
    private var arrayList: ArrayList<String>? = null
    private lateinit var adapter: DoctorPatientTestAdapter
    var testCategory = "heartRate"
    private var daysArray:List<Report> = ArrayList<Report>()
    private var userObj :User?=null
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
                R.layout.fragment_patient_profile,
                container,
                false
            )

        getTestHistory(testCategory)
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
            "ProfilePatient"
        )
        HomeActivityPateint.home.patientProfile()
        HomeActivityPateint.home.showBottomBar()
        arrayList = ArrayList()

        val mLayoutManager2: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvFitBit.layoutManager = mLayoutManager2
        (rvFitBit.itemAnimator as DefaultItemAnimator).supportsChangeAnimations =
            false


/*        adapter = DoctorPatientTestAdapter(requireActivity(), arrayList!!, this)
        arrayList?.addAll(Constants.arrDaysss)
        rvFitBit.adapter = adapter
        adapter.notifyDataSetChanged()*/
        /* rvFitBit.setLayoutManager(
             LinearLayoutManager(activity, RecyclerView.VERTICAL, false),
             R.layout.item_nearby_session_shimmer
         )
         rvFitBit.setItemViewType(ShimmerAdapter.ItemViewType { type: Int, position: Int -> R.layout.item_nearby_session_shimmer })
 */
    }
    
    
    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showSidebar(getBaseActivity(), "Patient")
        titleBar?.setTitle("PROFILE")


    }

    override fun setListeners() {
        val spinnerSingleSelectDialogFragment =
            SpinnerDialogFragment.newInstance(
                "Please Select",
                Constants.arrTest,
                object :
                    OnSpinnerOKPressedListener {
                    override fun onSingleSelection(data: SpinnerModel, selectedPosition: Int) {
                        /*  binding!!.txtQualification.setTextColor(
                              ContextCompat.getColor(
                                  activity!!,
                                  R.color.black
                              )
                          )*/
                        binding.txtHeadingHeartRate.text = data.text

                        if (data.positionInList ==0)
                        {
                            testCategory = "bloodGlucose"

                            getTestHistory(testCategory)

                        }
                        else if (data.positionInList ==1)
                        {
                            testCategory = "highPressure"
                            getTestHistory(testCategory)
                        }
                        else if (data.positionInList ==2)
                        {
                            testCategory = "lowPressure"
                            getTestHistory(testCategory)
                        }
                        else if (data.positionInList ==3)
                        {
                            testCategory = "heartRate"
                            getTestHistory(testCategory)
                        }
                        else if (data.positionInList ==4)
                        {
                            testCategory = "temperature"
                            getTestHistory(testCategory)
                        }
                    }
                },
                0
            )
        binding.relativeBgHeartRate.setOnClickListener {
            spinnerSingleSelectDialogFragment.show(
                requireActivity().supportFragmentManager,
                "SpinnerDialogFragmentSingle"
            )
        }
    }

    companion object {

        fun newInstance(): PatientProfileFragment {

            val args = Bundle()

            val fragment = PatientProfileFragment()
            fragment.arguments = args
            return fragment
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
            binding.txtAge.setText(userObj?.age.toString())

            if (!userObj?.image.isNullOrEmpty())
            {
                Picasso.get().load(userObj?.image).into(binding.ivImage)
            }


        }


    }



    private fun getTestHistory(category: String)
    {
        binding.pbHistory.visibility=View.VISIBLE

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.getTestHistory(category).enqueue(object: retrofit2.Callback<TestHistoryResponse> {
                override fun onResponse(
                    call: Call<TestHistoryResponse>,
                    response: Response<TestHistoryResponse>
                )
                {

                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbHistory.visibility=View.GONE


                    })

                    Log.d("Response", response.message())

                    try {



                        if (response.isSuccessful) {
                            Log.d("Response", response.body()!!.status.toString())
                            if (response.body()?.status==1)
                            {
                                Log.d("Response", response.body()!!.status.toString())

                                Log.d("Response", response.body()!!.data.toString())

                                Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()


                                if (!response.body()?.data?.report.isNullOrEmpty())
                                {
                                    daysArray = response.body()?.data?.report!!
                                    adapter = DoctorPatientTestAdapter( requireActivity(),daysArray!!, this@PatientProfileFragment)
                                    binding.rvFitBit.adapter = adapter
                                    adapter.notifyDataSetChanged()

                                }



                            }
                            else
                            {
                                activity?.runOnUiThread(java.lang.Runnable {
                                    Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_SHORT).show()

                                })

                            }

                        }
                        else {
                            activity?.runOnUiThread(java.lang.Runnable {
                                Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_SHORT).show()

                            })
                        }
                    }
                    catch (e:Exception)
                    {
                        activity?.runOnUiThread(java.lang.Runnable {
                            binding.pbHistory.visibility=View.GONE
                            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
                            Log.d("error", e.message.toString())

                        })
                    }
                }

                override fun onFailure(call: Call<TestHistoryResponse>, t: Throwable)
                {

                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbHistory.visibility=View.GONE
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()

                    })
                }
            })

        }


    }


    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            /* if (isLoading == null) return
             if (isLoading) {
                 rvFitBit?.showShimmer()
             } else {
                 rvFitBit?.hideShimmer()
             }*/
        }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
    }

    override fun onNothingSelected() {

    }

}