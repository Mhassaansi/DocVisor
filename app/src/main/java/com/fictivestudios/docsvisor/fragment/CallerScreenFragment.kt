package com.fictivestudios.docsvisor.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.HomeActivityPateint
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.CommonResponse
import com.fictivestudios.docsvisor.enums.FCMEnums
import com.fictivestudios.docsvisor.helper.PreferenceUtils
import com.fictivestudios.docsvisor.helper.REJECT_CALL
import com.fictivestudios.docsvisor.helper.USER_OBJECT
import com.fictivestudios.docsvisor.model.RejectCallRequestBody
import com.fictivestudios.docsvisor.model.User
import com.fictivestudios.docsvisor.widget.TitleBar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_accpet_reject.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CallerScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CallerScreenFragment : BaseFragment() {
    private var userObj: User? = null

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mView: View

    var receiverUserId:String=""
    private  var otherUserrName: String?=null
    var outSidetoken:String=""
    var channelName = ""
    var type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun setTitlebar(titleBar: TitleBar?) {

        activity?.let { titleBar?.hideBottomBar(it) }
    }

    override fun setListeners() {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView =inflater.inflate(R.layout.activity_accpet_reject, container, false)




        receiverUserId = arguments?.getString("user").toString()
        Log.d("args",arguments?.getString("receiverUserId").toString())



        outSidetoken = arguments?.getString("token").toString()
        Log.d("args",arguments?.getString("token").toString())



        channelName = arguments?.getString("channel").toString()
        Log.d("args",arguments?.getString("channel").toString())

        otherUserrName = arguments?.getString("name").toString()

        type = arguments?.getString("type").toString()





        mView.username.setText(otherUserrName +" Is Calling")


        var gson =  Gson()
        var json:String= PreferenceUtils.getString(USER_OBJECT)
        userObj=gson.fromJson(json, User::class.java)

        mView.btn_accept_call.setOnClickListener {


            if (receiverUserId!=null)
            {
                val bundle = bundleOf(
                    "token" to outSidetoken
                    ,"channel" to channelName
                    ,"name" to otherUserrName)


                if (type == FCMEnums.AUDIO.value)
                {


                    HomeActivityPateint.navControllerPatient?.navigate(R.id.audioFragment2,bundle)
                }
                else
                {
                    HomeActivityPateint.navControllerPatient?.navigate(R.id.videoFragment2,bundle)
                }

            }


        }

        mView.btn_end_call.setOnClickListener {

            rejectCall(userObj?.id.toString(),receiverUserId,channelName)

        }

        return mView
    }



    private fun rejectCall(senderId: String, receiverId: String, outSidetoken: String)
    {



        val apiClient = ApiClient.RetrofitInstance.getAgoraApiService(requireContext())

        val rejectCallRequestBody = RejectCallRequestBody(
            senderId,
            receiverId,
            outSidetoken,
            "call_reject"
        )

/*        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("sender_id", senderId )
            .addFormDataPart("receiver_id", senderId )
            .addFormDataPart("channel",outSidetoken )
            .addFormDataPart("type"," call_reject" )
            .build()*/

        GlobalScope.launch(Dispatchers.IO)
        {
            apiClient.rejectCall(rejectCallRequestBody).enqueue(object: retrofit2.Callback<CommonResponse> {
                override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>
                )
                {


                    activity?.runOnUiThread(java.lang.Runnable {



                    })

                    Log.d("statusCode",response?.body()?.status.toString())
                    response?.body()?.message?.let { Log.d("Response", it) }

                    Toast.makeText(requireContext(), ""+response?.body()?.message, Toast.LENGTH_SHORT).show()
                    try {


                        if (response.isSuccessful) {

                            if (response.body()?.status == 0) {
                                activity?.runOnUiThread(java.lang.Runnable {

                                    HomeActivityPateint?.navControllerPatient?.navigate(R.id.patientHomeFragment)
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


                        Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()

                    })
                }
            })

        }


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CallerScreenFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CallerScreenFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            try {
                if (intent.getStringExtra(REJECT_CALL).equals(REJECT_CALL)) {
                    activity?.onBackPressed()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            mMessageReceiver,
            IntentFilter("MyData")
        )
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mMessageReceiver)
    }


}