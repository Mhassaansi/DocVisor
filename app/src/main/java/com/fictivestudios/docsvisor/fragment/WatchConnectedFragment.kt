package com.fictivestudios.docsvisor.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.HomeActivityPateint
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.WatchBindResponse
import com.fictivestudios.docsvisor.helper.PreferenceUtils
import com.fictivestudios.docsvisor.helper.USER_OBJECT
import com.fictivestudios.docsvisor.model.User
import com.fictivestudios.docsvisor.widget.TitleBar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_q_r.view.*
import kotlinx.android.synthetic.main.fragment_watch_connected.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
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
 * Use the [WatchConnectedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WatchConnectedFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mView: View


    private var userObj : User?=null

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
        titleBar?.visibility = View.VISIBLE
        titleBar!!.setbtnrightgone()
        ///titleBar?.showSidebar(getBaseActivity(), "Patient")
        titleBar?.setTitle("WATCH CONNECTIVITY")
    }

    override fun setListeners() {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_watch_connected, container, false)

        mView.btn_unbind.isEnabled = false
        var task = GlobalScope.async(Dispatchers.IO) {
            var gson =  Gson()
            var json:String= PreferenceUtils.getString(USER_OBJECT)
            userObj=gson.fromJson(json, User::class.java)

        }
        GlobalScope.launch(Dispatchers.Main) {
            task.await()
            mView.btn_unbind.isEnabled = true
        }

        mView.btn_unbind.setOnClickListener {

            userObj?.watch_imei?.let { it1 -> unBind(it1,"unbind") }

        }

        mView.btn_recalibrate.setOnClickListener {

            HomeActivityPateint.navControllerPatient?.navigate(R.id.calibrateFragment)
        }
        return mView
    }


    private fun unBind(imei: String,type:String) {

        mView.pbWatch.visibility = View.VISIBLE


        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)

            .addFormDataPart("imei",imei)
            .addFormDataPart("type",type)
            .build()


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.checkBound(requestBody).enqueue(object: retrofit2.Callback<WatchBindResponse> {
                override fun onResponse(
                    call: Call<WatchBindResponse>,
                    response: Response<WatchBindResponse>
                )
                {
                      activity?.runOnUiThread(java.lang.Runnable {
                          mView.pbWatch.visibility=View.GONE

                      })

                    Log.d("Response", response.body()?.message.toString())

                    try {


                        if (response.isSuccessful) {

                            if (response.body()?.status == 1)
                            {
                                Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()

                                val responseData: User? = response?.body()?.data
                                val gson = Gson()
                                val json:String = gson.toJson(responseData)
                                PreferenceUtils.saveString(USER_OBJECT,json)
                                HomeActivityPateint.navControllerPatient?.navigate(R.id.homeActivityPateint)
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
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<WatchBindResponse>, t: Throwable)
                {


                     activity?.runOnUiThread(java.lang.Runnable {
                         mView.pbWatch.visibility=View.GONE
                         Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
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
         * @return A new instance of fragment WatchConnectedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WatchConnectedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}