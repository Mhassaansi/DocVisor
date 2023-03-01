package com.fictivestudios.docsvisor.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.fictivestudios.docsvisor.adapter.DoctorListAdapter
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.DoctorData
import com.fictivestudios.docsvisor.apiManager.response.DoctorResponse
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.databinding.FragmentDoctorListBinding
import com.fictivestudios.docsvisor.helper.doctorData
import com.fictivestudios.docsvisor.widget.TitleBar
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.fragment_doctor_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class ListOfDoctorsFragment : BaseFragment(), OnItemClickListener,
    OnChartValueSelectedListener {

    lateinit var binding: FragmentDoctorListBinding
    var validator: Validator? = null
    private var arrayList: ArrayList<DoctorData>? = null
    private lateinit var adapter: DoctorListAdapter

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
                R.layout.fragment_doctor_list,
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

/*        arrayList = ArrayList()
        adapter = DoctorListAdapter(requireActivity(), arrayList!!, this)
        arrayList?.addAll(Constants.arrDaysss)
        rvDocList.adapter = adapter
        adapter.notifyDataSetChanged()*/
        val mLayoutManager2: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvDocList.layoutManager = mLayoutManager2
        (rvDocList.itemAnimator as DefaultItemAnimator).supportsChangeAnimations =
            false

        getDoctersList()

        /* rvDocList.setLayoutManager(
             LinearLayoutManager(activity, RecyclerView.VERTICAL, false),
             R.layout.item_nearby_session_shimmer
         )
         rvDocList.setItemViewType(ShimmerAdapter.ItemViewType { type: Int, position: Int -> R.layout.item_nearby_session_shimmer })
 */
    }
    
    
    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showSidebar(getBaseActivity(), "Patient")
        titleBar?.setTitle("DOCTOR LISTS")


    }

    override fun setListeners() {

    }


    private fun getDoctersList()
    {

        binding.pbDoctor.visibility=View.VISIBLE

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())

        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.doctor().enqueue(object: retrofit2.Callback<DoctorResponse> {
                override fun onResponse(
                    call: Call<DoctorResponse>,
                    response: Response<DoctorResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbDoctor.visibility=View.GONE

                    })

                    Log.d("scheduleResponse", response.message())

                    try {


                        if (response.isSuccessful) {
                            arrayList = ArrayList()
                            arrayList = response.body()?.data

                            adapter = DoctorListAdapter(requireActivity(), arrayList!!, this@ListOfDoctorsFragment)

                            binding.rvDocList.adapter = adapter
                            adapter.notifyDataSetChanged()

                            binding.txtHeadingHeartRate.addTextChangedListener(object: TextWatcher{
                                override fun beforeTextChanged(
                                    p0: CharSequence?,
                                    p1: Int,
                                    p2: Int,
                                    p3: Int
                                ) {

                                }

                                override fun onTextChanged(
                                    p0: CharSequence?,
                                    p1: Int,
                                    p2: Int,
                                    p3: Int
                                ) {

                                }

                                override fun afterTextChanged(p0: Editable?) {
                                  adapter.filter.filter(p0)
                                }


                            })




                        }
                        else {
                            Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                        }
                    }
                    catch (e:Exception)
                    {
                        //Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DoctorResponse>, t: Throwable)
                {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                     activity?.runOnUiThread(java.lang.Runnable {
                         binding.pbDoctor.visibility=View.GONE

                     })
                }
            })

        }


    }




    companion object {

        fun newInstance(): ListOfDoctorsFragment {

            val args = Bundle()

            val fragment = ListOfDoctorsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onItemClick(position: Int, `object`: Any?, view: View?, adapterName: String?) {


        if (arrayList?.get(position) != null)
        {
            doctorData = arrayList?.get(position)
/*            doctorData = arrayList?.get(position)
            val bundle = bundleOf("doctorData" to doctorData)*/

            HomeActivityPateint.navControllerPatient!!.navigate(R.id.doctorsProfileCertificateFragment)
        }



    }


    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            /* if (isLoading == null) return
             if (isLoading) {
                 rvDocList?.showShimmer()
             } else {
                 rvDocList?.hideShimmer()
             }*/
        }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
    }

    override fun onNothingSelected() {

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