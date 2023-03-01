package com.fictivestudios.docsvisor.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.ilhasoft.support.validation.Validator
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.HomeActivityPateint
import com.fictivestudios.docsvisor.adapter.RequestDoctorAdapter
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.AlarmResponse
import com.fictivestudios.docsvisor.apiManager.response.CommonResponse
import com.fictivestudios.docsvisor.apiManager.response.PendingAppointment
import com.fictivestudios.docsvisor.apiManager.response.PendingAppointmentData
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.databinding.DoctorRequestlist
import com.fictivestudios.docsvisor.widget.TitleBar
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.fragment_doctor_list.*
import kotlinx.android.synthetic.main.fragment_request_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class RequestDoctorFragment : BaseFragment(), OnItemClickListener,
    OnChartValueSelectedListener {

    lateinit var binding: DoctorRequestlist
    var validator: Validator? = null
    private var arrayList: ArrayList<PendingAppointmentData>? = null
    private lateinit var adapter: RequestDoctorAdapter

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
                 R.layout.fragment_request_list,
                 container,
                false
            )

        getAppointmentList()
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
        binding.rvReqList.layoutManager = mLayoutManager2
        (binding.rvReqList.itemAnimator as DefaultItemAnimator).supportsChangeAnimations =
            false


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
        titleBar?.setTitle("APPOINTMENTS")
        titleBar.setbackbtn(activity)

    }

    override fun setListeners() {

    }

    companion object {

        fun newInstance(): RequestDoctorFragment {
            val args = Bundle()
            val fragment = RequestDoctorFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onItemClick(position: Int, `object`: Any?, view: View?, adapterName: String?) {

        if ((view as AppCompatButton).text.toString() == "Accept")
        {
            acceptAppointmentRequest(arrayList?.get(position)?.id.toString(),position,"accept")

        }
            else{
            acceptAppointmentRequest(arrayList?.get(position)?.id.toString(),position,"reject")
            }
       /* HomeActivityPateint.navControllerPatient!!.navigate(R.id.doctorsProfileCertificateFragment)*/

    }


    private fun getAppointmentList()
    {

        binding.pbPending.visibility=View.VISIBLE

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.getPendingAppointment("1").enqueue(object: retrofit2.Callback<PendingAppointment> {
                override fun onResponse(
                    call: Call<PendingAppointment>,
                    response: Response<PendingAppointment>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbPending.visibility=View.GONE

                    })

                    Log.d("Response", response.message())

                    try {


                        if (response.isSuccessful) {

                            if (response.body()?.status==1)
                            {
                                arrayList = ArrayList()
                                arrayList = response.body()?.data as ArrayList
                                adapter = RequestDoctorAdapter(requireActivity(), arrayList!!, this@RequestDoctorFragment)
                                binding.rvReqList.adapter = adapter
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

                override fun onFailure(call: Call<PendingAppointment>, t: Throwable)
                {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    /* activity?.runOnUiThread(java.lang.Runnable {
                         binding.pbChangePass.visibility=View.GONE
                         binding.btnSignUp.isEnabled=true
                         binding.btnSignUp.text="RESET"

                     })*/
                }
            })

        }


    }

    private fun acceptAppointmentRequest(bookingId: String,position: Int, status: String)
    {

        binding.pbPending.visibility=View.VISIBLE

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)

            .addFormDataPart("booking_id",bookingId)
            .addFormDataPart("status", status)

            .build()


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.acceptAppointment(requestBody).enqueue(object: retrofit2.Callback<CommonResponse> {
                override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbPending.visibility=View.GONE

                    })

                    Log.d("Response", response.body()?.message.toString())

                    try {


                        if (response.isSuccessful) {


                            Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()

                            if (response.body()?.status==1)
                            {
                                arrayList?.remove(arrayList!![position])
                                adapter.notifyDataSetChanged()
                                adapter.notifyItemRemoved(position)
                            }
                        }
                        else {
                            Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    catch (e:Exception)
                    {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CommonResponse>, t: Throwable)
                {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    /* activity?.runOnUiThread(java.lang.Runnable {
                         binding.pbChangePass.visibility=View.GONE
                         binding.btnSignUp.isEnabled=true
                         binding.btnSignUp.text="RESET"

                     })*/
                }
            })

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


    override fun onResume() {
        super.onResume()

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