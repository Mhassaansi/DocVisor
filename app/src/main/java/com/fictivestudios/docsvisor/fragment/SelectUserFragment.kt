package com.fictivestudios.docsvisor.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.MainActivity
import com.fictivestudios.docsvisor.constants.LOGIN_TYPE
import com.fictivestudios.docsvisor.constants.SELECT_USER
import com.fictivestudios.docsvisor.databinding.FragmentSelectUserBinding
import com.fictivestudios.docsvisor.widget.TitleBar
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.client.SessionManager
import com.fictivestudios.docsvisor.apiManager.response.LoginResponse
import com.fictivestudios.docsvisor.helper.*
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class SelectUserFragment : BaseFragment() {

    private var sessionManager: SessionManager?=null
    lateinit var binding: FragmentSelectUserBinding
    lateinit var name: String
    var socialName:String?=null
    var email:String? = null

    var socialToken:String? = null
    var loginType:String? = null
    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_select_user, container, false)

        sessionManager = SessionManager(requireContext())
        socialToken = arguments?.getString(SOCIAL_TOKEN).toString()
        loginType = arguments?.getString(LOGIN_TYPE).toString()
        socialName = arguments?.getString(SOCIAL_NAME).toString()
        email = arguments?.getString(EMAIL).toString()
        Log.d("loginType",loginType.toString())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        YoYo.with(Techniques.FadeInDown)
            .duration(700)
            .repeat(0)
            .playOn(binding!!.linearLayoutCompatDoctor);
        YoYo.with(Techniques.FadeInDown)
            .duration(1200)
            .repeat(0)
            .playOn(binding!!.linearLayoutCompatPatient);

    }

    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.setTitle("SELECT USER")
    }

    override fun setListeners() {

        sharedPreferenceManager?.putValue(
            SELECT_USER,
            "Doctor"
        )

        binding.linearLayoutCompatDoctor.setOnClickListener {

/*            sharedPreferenceManager?.putValue(
                SELECT_USER,
                "Doctor"
            )



            // Log.d("nammeeeeeeee",sharedPreferenceManager?.getString(SELECT_USER).toString())
            callDialog(name = "Doctor")*/

            if (PreferenceUtils.getBoolean(IS_SOCIAL_LOGIN))
            {
                if (socialToken!=null || socialToken?.equals("null") == true && loginType!=null || loginType?.equals("null") == true)
                {
                    socialToken?.let { it1 -> loginType?.let { it2 -> socialName?.let { it3 ->
                        email?.let { it4 ->
                            login(it1, it2,"doctor",
                                it3, it4
                            )
                        }
                    } } }
                }
            }
            else
            {
                MainActivity.navController.navigate(R.id.loginFragment)
            }




        }

        binding.linearLayoutCompatPatient.setOnClickListener {


            sharedPreferenceManager?.putValue(
                SELECT_USER,
                "Patient"
            )

      /*
            //Log.d("nammeeeeeeee",sharedPreferenceManager?.getString(SELECT_USER).toString())

            callDialog(name = "Patient")*/


            if (PreferenceUtils.getBoolean(IS_SOCIAL_LOGIN)) {
                if (socialToken!=null || socialToken?.equals("null") == true && loginType!=null || loginType?.equals("null") == true )
                {


                socialToken?.let { it1 ->
                    loginType?.let { it2 ->
                        socialName?.let { it3 ->
                            email?.let { it4 ->
                                login(
                                    it1,
                                    it2,
                                    "patient",
                                    it3,
                                    it4
                                )
                            }
                        }
                    }
                }
            } }
            else
            {
                MainActivity.navController.navigate(R.id.loginFragment)
            }




        }

    }

    private fun callDialog(name: String) {
   /*     val bundle = Bundle()
        if (sharedPreferenceManager?.getString(LOGIN_TYPE) == "Facebook"  || sharedPreferenceManager?.getString(LOGIN_TYPE) == "Google") {
            MainActivity.navController.navigate(R.id.verificationFragment)
        } else {



        }
*/
        //MainActivity.navController.navigate(R.id.loginFragment)


    }

    private fun login(socialToken: String, loginType: String, role: String, socialName: String,socialEmail:String)
    {
        binding.pbSelect.visibility=View.VISIBLE


        var token = PreferenceUtils.getString(FCM,"")

        Log.d("fcmToken",token.toString())
        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)



            .addFormDataPart("provider", loginType)
            .addFormDataPart("access_token", socialToken)
            .addFormDataPart("device_type", "android")
            .addFormDataPart("device_token", token.toString())
            .addFormDataPart("role", role)
            .addFormDataPart("displayName", socialName)
            .addFormDataPart("email", socialEmail)
            .build()


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.socialLogin(requestBody).enqueue(object: retrofit2.Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbSelect.visibility=View.GONE


                    })



                    //  try {
/*                    loginProg.visibility=View.GONE
                    loginBtn.isEnabled=true*/

                    response.body()?.message?.let { Log.d("response", it) }
                    response.body()?.data?.let { Log.d("response", it.toString()) }
                    if (response.body()!=null)
                    {

                        val loginResponse: LoginResponse? =response.body()
                        val statuscode= loginResponse?.status
                        loginResponse?.message?.let { Log.d("response", it) }



                        if (statuscode==1)
                        {
                            if (role=="doctor")
                            {

                               // callDialog(name = "Doctor")

                                val gson = Gson()
                                val json:String = gson.toJson(loginResponse.data )
                                PreferenceUtils.saveString(USER_OBJECT,json)
                                sessionManager?.saveAuthToken(loginResponse.bearer_token)




                                if (role == "doctor")
                                {                        //    getBaseActivity().finish()
                                    MainActivity.navController.navigate(R.id.homeActivity)
                                }
                                else if (role == "patient")
                                {                            //getBaseActivity().finish()
                                    MainActivity.navController.navigate(R.id.homeActivityPateint)
                                }
                            }
                            else{

                                val gson = Gson()
                                val json:String = gson.toJson(loginResponse.data )
                                PreferenceUtils.saveString(USER_OBJECT,json)
                                sessionManager?.saveAuthToken(loginResponse.bearer_token)


                                if (role == "doctor")
                                {                        //    getBaseActivity().finish()
                                    MainActivity.navController.navigate(R.id.homeActivity)
                                }
                                else if (role == "patient")
                                {                            //getBaseActivity().finish()
                                    MainActivity.navController.navigate(R.id.homeActivityPateint)
                                }
                              //  callDialog(name = "Patient")
                            }

                        }



                        else {


                            activity?.runOnUiThread(java.lang.Runnable {
                                Toast.makeText(context, response?.body()?.message, Toast.LENGTH_SHORT).show()
                            })
                        }
                    }
                    //  }
                    /*      catch (e:Exception)
                          {

                              Log.d("exception",e.localizedMessage)
                              activity?.runOnUiThread(java.lang.Runnable {
      //                            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                              })
                          }*/
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable)
                {


                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbSelect.visibility=View.GONE
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    })
                }
            })

        }


    }




    companion object {

        fun newInstance(): SelectUserFragment {

            val args = Bundle()
            val fragment = SelectUserFragment()
            fragment.arguments = args
            return fragment
        }
    }


}