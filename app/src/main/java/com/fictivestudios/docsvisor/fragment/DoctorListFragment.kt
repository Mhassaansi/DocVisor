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
import com.fictivestudios.docsvisor.adapter.AppointmentListadapter
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.AppointmentData
import com.fictivestudios.docsvisor.apiManager.response.ViewAppointmentResponse
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.databinding.Doctorlist
import com.fictivestudios.docsvisor.widget.TitleBar
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.fragment_doctor_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class DoctorListFragment : BaseFragment(), OnItemClickListener,
    OnChartValueSelectedListener {

    lateinit var binding: Doctorlist
    var validator: Validator? = null
    private var arrayList: List<AppointmentData>? = null
    private lateinit var adapter: AppointmentListadapter

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
                R.layout.doctorlist,
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


        val mLayoutManager2: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvDocList.layoutManager = mLayoutManager2
        (rvDocList.itemAnimator as DefaultItemAnimator).supportsChangeAnimations =
            false


        getAppointmentList()

        /* rvDocList.setLayoutManager(
             LinearLayoutManager(activity, RecyclerView.VERTICAL, false),
             R.layout.item_nearby_session_shimmer
         )
         rvDocList.setItemViewType(ShimmerAdapter.ItemViewType { type: Int, position: Int -> R.layout.item_nearby_session_shimmer })
 */
    }


    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar!!.setbtnrightgone()
        titleBar.setbackbtn(activity)
        titleBar?.setTitle("APPOINTMENTS")


    }

    override fun setListeners() {

    }


    private fun getAppointmentList()
    {

        binding.pbDocList.visibility = View.VISIBLE

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.getAppointment().enqueue(object: retrofit2.Callback<ViewAppointmentResponse> {
                override fun onResponse(
                    call: Call<ViewAppointmentResponse>,
                    response: Response<ViewAppointmentResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbDocList.visibility=View.GONE

                    })

                    Log.d("Response", response.message())

                    try {

                        arrayList = response.body()?.data
                        if (response.isSuccessful) {

                            if (response.body()?.status==1)
                            {
                                Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                                arrayList = ArrayList()
                                arrayList = response.body()?.data
                                adapter = AppointmentListadapter(requireActivity(), arrayList!!, this@DoctorListFragment)
                                rvDocList.adapter = adapter
                                adapter.notifyDataSetChanged()
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

                override fun onFailure(call: Call<ViewAppointmentResponse>, t: Throwable)
                {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                     activity?.runOnUiThread(java.lang.Runnable {
                         binding.pbDocList.visibility=View.GONE

                     })
                }
            })

        }


    }



    companion object {
             fun newInstance(): DoctorListFragment {
             val args = Bundle()
             val fragment = DoctorListFragment()
             fragment.arguments = args
             return fragment
         }
    }

    override fun onItemClick(position: Int, `object`: Any?, view: View?, adapterName: String?) {
     //  HomeActivityPateint.navControllerPatient!!.navigate(R.id.doctorsProfileCertificateFragment)


    }


    override fun onResume() {
        super.onResume()
        getAppointmentList()
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