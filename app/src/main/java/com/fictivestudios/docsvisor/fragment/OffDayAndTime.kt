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
import com.fictivestudios.docsvisor.adapter.OffdaysAdapter
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.CommonResponse
import com.fictivestudios.docsvisor.apiManager.response.OffDaysData

import com.fictivestudios.docsvisor.apiManager.response.OffDaysRes
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.constants.Constants
import com.fictivestudios.docsvisor.databinding.Offdayssbind

import com.fictivestudios.docsvisor.widget.TitleBar
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.offdaysandtime.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class OffDayAndTime : BaseFragment(), OnItemClickListener,
    OnChartValueSelectedListener {

    lateinit var binding: Offdayssbind
    var validator: Validator? = null
    private var arrayList: ArrayList<OffDaysData>? = null
    private lateinit var adapter: OffdaysAdapter

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
                R.layout.offdaysandtime,
                container,
                false
            )

        getOffDays()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val mLayoutManager2: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvoffdaysList.layoutManager = mLayoutManager2
        (rvoffdaysList.itemAnimator as DefaultItemAnimator).supportsChangeAnimations =
            false


    }


    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE

        titleBar?.showBackButtonWithAddButton(activity,"OFFDAYS","OFFDAYS")
//        titleBar?.setBtnRight(R.drawable.add, View.OnClickListener {
//             dailog()
//        })



       // titleBar?.showSidebar(getBaseActivity(), "Patient")
        titleBar?.setTitle("Off Days And Time")
    }


    override fun setListeners() {

    }

    companion object {
        fun newInstance(): Subscription {
            val args = Bundle()
            val fragment = Subscription()
            fragment.arguments = args
            return fragment
        }
    }

    private fun getOffDays()
    {


        binding.pbDays.visibility = View.VISIBLE

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.getOffDays().enqueue(object: retrofit2.Callback<OffDaysRes> {
                override fun onResponse(
                    call: Call<OffDaysRes>,
                    response: Response<OffDaysRes>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbDays.visibility=View.GONE

                    })

                    Log.d("Response", response.message())

                    try {


                        if (response.isSuccessful) {

                            if (response.body()?.status==1) {
                                Log.d("Response", response.body()!!.message)

                                Log.d("Response", response.body()!!.data.toString())

                                Toast.makeText(
                                    context,
                                    response.body()?.message,
                                    Toast.LENGTH_SHORT
                                ).show()



                                arrayList = ArrayList()
                                arrayList = response.body()?.data as ArrayList<OffDaysData>
                                adapter = OffdaysAdapter(requireActivity(), arrayList!!, this@OffDayAndTime)
                                rvoffdaysList.adapter = adapter
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

                override fun onFailure(call: Call<OffDaysRes>, t: Throwable)
                {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                     activity?.runOnUiThread(java.lang.Runnable {
                         binding.pbDays.visibility=View.GONE

                     })
                }
            })

        }


    }

    private fun deleteOffDays(Id: String, position: Int)
    {

          binding.pbDays?.visibility = View.VISIBLE

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("off_day_id", Id)
            .build()

        GlobalScope.launch(Dispatchers.IO)
        {
            apiClient.deleteOffDays(requestBody).enqueue(object: retrofit2.Callback<CommonResponse> {
                override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                          binding.pbDays?.visibility=View.GONE


                    })

                    response?.body()?.message?.let { Log.d("Response", it) }

                    try {


                        if (response.isSuccessful) {

                            if (response.body()?.status == 1) {
                                activity?.runOnUiThread(java.lang.Runnable {

                                    arrayList?.removeAt(position)
                                    adapter.notifyItemRemoved(position)
                                    adapter.notifyDataSetChanged()

                                  //  getOffDays()

                                    Toast.makeText(requireContext(), response?.body()?.message, Toast.LENGTH_SHORT).show()

                                })
                            }


                        }
                        else {
                            activity?.runOnUiThread(java.lang.Runnable {

                                Toast.makeText(requireContext(), response.message(), Toast.LENGTH_SHORT).show()

                            })
                        }
                    }
                    catch (e:Exception)
                    {

                        activity?.runOnUiThread(java.lang.Runnable {

                            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()

                        })
                    }
                }

                override fun onFailure(call: Call<CommonResponse>, t: Throwable)
                {

                    activity?.runOnUiThread(java.lang.Runnable {

                          binding.pbDays?.visibility = View.GONE
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()


                    })
                }
            })

        }


    }

    override fun onItemClick(position: Int, `object`: Any?, view: View?, adapterName: String?) {
       // HomeActivityPateint.navControllerPatient.navigate(R.id.doctorsProfileCertificateFragment)

        Log.d("deleteId",arrayList?.get(position)?.id.toString())
        deleteOffDays(arrayList?.get(position)?.id.toString(),position)

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

    override fun onResume() {
        super.onResume()
        getOffDays()
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