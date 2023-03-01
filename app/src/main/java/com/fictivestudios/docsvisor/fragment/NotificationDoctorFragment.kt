package com.fictivestudios.docsvisor.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.adapter.NotificationDoctorAdapter
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.CommonResponse
import com.fictivestudios.docsvisor.apiManager.response.GetNotificationData
import com.fictivestudios.docsvisor.apiManager.response.GetNotificationResponse
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.databinding.FragmentDoctorNotificationBinding
import com.fictivestudios.docsvisor.widget.TitleBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

open class NotificationDoctorFragment : BaseFragment(), OnItemClickListener{

    private var binding: FragmentDoctorNotificationBinding? = null

    private  var notificationDoctorAdapter: NotificationDoctorAdapter? = null
    private var arrayNotification: ArrayList<GetNotificationData> =  ArrayList()

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_doctor_notification, container, false);

        getNotificationList()
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        bindData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (notificationDoctorAdapter != null) {
            notificationDoctorAdapter?.saveStates(outState)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (notificationDoctorAdapter != null) {
            notificationDoctorAdapter?.restoreStates(savedInstanceState)
        }
    }


    private fun bindData() {


        val mLayoutManager1: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        binding!!.rvNotification.layoutManager = mLayoutManager1

        (binding!!.rvNotification.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false


    }


    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showBackButton(activity,"NOTIFICATIONS")
        titleBar?.hideBottomBar(requireActivity())

    }

    override fun setListeners() {

    }




    private fun getNotificationList()
    {

        binding?.pbGetnoti?.visibility=View.VISIBLE

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.getNotificationList().enqueue(object: retrofit2.Callback<GetNotificationResponse> {
                override fun onResponse(
                    call: Call<GetNotificationResponse>,
                    response: Response<GetNotificationResponse>
                )
                {



                    activity?.runOnUiThread(java.lang.Runnable {
                        binding?.pbGetnoti?.visibility=View.GONE


                    })

                    Log.d("Response", response.message())

                    try {


                        if (response.isSuccessful) {
                            Log.d("Response", response.body()!!.status.toString())
                            if (response.body()?.status==1)
                            {
                                Log.d("Response", response.body()?.status.toString())

                                Log.d("Response", response.body()?.data.toString())

                                Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()

                                arrayNotification = response.body()?.data!! as ArrayList<GetNotificationData>

                                if (!arrayNotification?.isNullOrEmpty())
                                {

                                    notificationDoctorAdapter = NotificationDoctorAdapter(requireContext(), arrayNotification!!, this@NotificationDoctorFragment)
                                    binding!!.rvNotification.adapter = notificationDoctorAdapter

                                    notificationDoctorAdapter?.notifyDataSetChanged()
                                }









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
                        activity?.runOnUiThread(java.lang.Runnable {
                            binding?.pbGetnoti?.visibility=View.GONE
                            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()

                        })
                    }
                }

                override fun onFailure(call: Call<GetNotificationResponse>, t: Throwable)
                {

                    activity?.runOnUiThread(java.lang.Runnable {
                        binding?.pbGetnoti?.visibility=View.GONE
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()

                    })
                }
            })

        }


    }




    companion object {
        fun newInstance(): NotificationDoctorFragment {
            val args = Bundle()
            val fragment = NotificationDoctorFragment()
            fragment.arguments = args
            return fragment
        }
    }




    override fun onItemClick(position: Int, `object`: Any?, view: View?, adapterName: String?) {

        deleteNotfication(arrayNotification?.get(position)?.id.toString(),position)


    }
    private fun deleteNotfication(notification_id: String, position: Int)
    {

        binding?.pbGetnoti?.visibility = View.VISIBLE

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("notification_id", notification_id )
            .build()

        GlobalScope.launch(Dispatchers.IO)
        {
            apiClient.deleteNotification(requestBody).enqueue(object: retrofit2.Callback<CommonResponse> {
                override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>
                )
                {


                    activity?.runOnUiThread(java.lang.Runnable {
                        binding?.pbGetnoti?.visibility=View.GONE


                    })

                    Log.d("Response", response.message())

                    try {


                        if (response.isSuccessful) {

                            if (response.body()?.status == 1) {
                                activity?.runOnUiThread(java.lang.Runnable {

                                    arrayNotification.removeAt(position)
                                    notificationDoctorAdapter?.notifyItemRemoved(position)
                                    notificationDoctorAdapter?.notifyDataSetChanged()
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

                        binding?.pbGetnoti?.visibility = View.GONE
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()


                    })
                }
            })

        }


    }

}