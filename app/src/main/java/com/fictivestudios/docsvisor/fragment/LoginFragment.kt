package com.fictivestudios.docsvisor.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import br.com.ilhasoft.support.validation.Validator
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.MainActivity
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.client.SessionManager
import com.fictivestudios.docsvisor.apiManager.response.LoginResponse
import com.fictivestudios.docsvisor.constants.SELECT_USER
import com.fictivestudios.docsvisor.databinding.FragmentLoginBinding
import com.fictivestudios.docsvisor.helper.*
import com.fictivestudios.docsvisor.helper.Validations.Companion.isValidPassword
import com.fictivestudios.docsvisor.widget.TitleBar
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class LoginFragment : BaseFragment(), Validator.ValidationListener {

    lateinit var binding: FragmentLoginBinding
    lateinit var name: String

    lateinit var sessionManager: SessionManager

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);


/*
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("fcm", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                PreferenceUtils.saveString(FCM,token.toString())
                // Log and toast

                Log.d("fcmtoken", token.toString())
                Toast.makeText(requireContext(), "token generated succesfuly", Toast.LENGTH_SHORT).show()
            })

*/

/*
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            PreferenceUtils.saveString(FCM,token.toString())
            // Log and toast

            Log.d("fcmtoken", token.toString())


        })*/

        sessionManager = SessionManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.setTitle("LOGIN")
    }

    override fun setListeners() {

        binding.btnLogin.setOnClickListener {



            validateFields()

        }

        binding.txtSignUp.setOnClickListener {

            if (sharedPreferenceManager
                    ?.getString(SELECT_USER) == "Doctor"
            ) {
                MainActivity.navController.navigate(R.id.signUpFragment)
            } else {
                MainActivity.navController.navigate(R.id.signupPatientFragment)


            }
        }

        binding.txtForgetPass.setOnClickListener {
            MainActivity.navController.navigate(R.id.forgotPassFragment)
        }
    }


    companion object {

        fun newInstance(): LoginFragment {

            val args = Bundle()
            val fragment = LoginFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onValidationSuccess() {

    }

    override fun onValidationError() {
    }


    private fun login()
    {
        binding.pbLogin.visibility=View.VISIBLE
        binding.btnLogin.isEnabled=false
        binding.btnLogin.text=""

        var token = PreferenceUtils.getString(FCM,"")

        Log.d("fcmToken",token.toString())
        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)



            .addFormDataPart("email", binding.txtEmail.text.toString())
            .addFormDataPart("password", binding.txtPass.text.toString())
            .addFormDataPart("device_type", "android")
            .addFormDataPart("device_token", token.toString())
            .build()


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.login(requestBody).enqueue(object: retrofit2.Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbLogin.visibility=View.GONE
                        binding.btnLogin.isEnabled=true
                        binding.btnLogin.text="LOGIN"

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

                            val gson = Gson()
                            val json:String = gson.toJson(loginResponse.data )
                            PreferenceUtils.saveString(USER_OBJECT,json)
                            sessionManager.saveAuthToken(loginResponse.bearer_token)

                            PreferenceUtils.saveBoolean(IS_SOCIAL_LOGIN,false)

                            if (loginResponse?.data?.role?.equals(USER_TYPE_DOCTOR) && sharedPreferenceManager?.getString(SELECT_USER) == "Doctor")
                            {                        //    getBaseActivity().finish()
                                MainActivity.navController.navigate(R.id.homeActivity)
                            }
                            else if (loginResponse?.data?.role?.equals(USER_TYPE_PATIENT) && sharedPreferenceManager?.getString(SELECT_USER) == "Patient")
                            {                            //getBaseActivity().finish()
                                MainActivity.navController.navigate(R.id.homeActivityPateint)
                            }
                            else
                            {
                                activity?.runOnUiThread(java.lang.Runnable {
                                    Toast.makeText(context, "No account found for this role", Toast.LENGTH_SHORT).show()
                                })
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
                        binding.pbLogin.visibility=View.GONE
                        binding.btnLogin.isEnabled=true
                        binding.btnLogin.text="LOGIN"
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    })
                }
            })

        }


    }



    private fun validateFields() {


        if (!Validations.isValidEmail(binding.txtEmail.text.toString()))
        {
            binding.txtEmail.setError(getString(R.string.invalid_email_format))
            return
        }
        if (!binding.txtPass.text.toString().isValidPassword() )
        {
            binding.txtPass.setError(getString(R.string.password_must_be))
            Toast.makeText(requireContext(), "Password should be of 8 characters long (should contain uppercase, lowercase, number and special character", Toast.LENGTH_LONG).show()
            return
        }


             else
        {
            login()



        }
    }

}