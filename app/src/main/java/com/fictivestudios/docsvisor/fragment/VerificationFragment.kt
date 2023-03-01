package com.fictivestudios.docsvisor.fragment

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.MainActivity
import com.fictivestudios.docsvisor.databinding.FragmentVerificationBinding
import com.fictivestudios.docsvisor.widget.TitleBar
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.fictivestudios.docsvisor.activities.HomeActivityPateint
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.client.SessionManager
import com.fictivestudios.docsvisor.apiManager.response.ResendOTPResponse
import com.fictivestudios.docsvisor.apiManager.response.VerifyOTPResponse
import com.fictivestudios.docsvisor.constants.SELECT_USER
import com.fictivestudios.docsvisor.helper.*
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class VerificationFragment : BaseFragment() {

    private var binding: FragmentVerificationBinding? = null
    var userId = ""
    var nameFrag = ""

    //private lateinit var viewModel: VerificationViewModel
    var cTimer: CountDownTimer? = null
    var timer = 0

    lateinit var sessionManager: SessionManager
    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* viewModel =
             ViewModelProvider.AndroidViewModelFactory(Application())
                 .create<VerificationViewModel>(VerificationViewModel::class.java)*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_verification, container, false)
        /*binding!!.model = viewModel
        binding!!.lifecycleOwner = this
        userId = arguments?.getString("user_id")!!
        nameFrag = arguments?.getString("nameFrag")!!*/
        sessionManager = SessionManager(requireContext())
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startTimer()
    }


    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.setTitle("VERIFICATION")
    }


    override fun setListeners() {
        //viewModel.webResponse?.observe(this, WebResponseObserver())
        binding?.txtResend?.setOnClickListener {
            resendOTP()
        }




        binding?.etPinText?.setOnPinEnteredListener {

            PreferenceUtils.saveString(OTP, binding?.etPinText?.text.toString())
            verifyOTP()
        } /*= (object : PinField.OnTextCompleteListener {
            override fun onTextComplete(@NotNull enteredText: String): Boolean {
                when (nameFrag) {
                    "Forget" -> viewModel.verify(
                        activity!!, view!!, userId, binding?.etPinText?.text.toString(),
                        "21321312", DEVICE_OS_ANDROID, getBaseActivity(), "forgot"
                    )
                    else -> viewModel.verify(
                        activity!!, view!!, userId, binding?.etPinText?.text.toString(),
                        "21321312", DEVICE_OS_ANDROID, getBaseActivity(), "signup"
                    )
                }

                binding?.etPinText!!.setText("")

                return true // Return false to keep the keyboard open else return true to close the keyboard
            }
        })*/
    }



    fun startTimer() {
        if (cTimer != null) {
            cTimer!!.cancel()
        }
        timer = 0
        cTimer = object : CountDownTimer(60000, 1000) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            override fun onTick(millisUntilFinished: Long) {
                timer = Math.toIntExact(millisUntilFinished / 1000)
                binding!!.tvTimer.text = "00:$timer"
                binding!!.circularProgressBar.progress = timer.toFloat()
                binding!!.tvTimer.isEnabled = false
                binding!!.tvTimer.alpha = .4f
            }

            override fun onFinish() {
                YoYo.with(Techniques.FadeIn)
                    .duration(700)
                    .repeat(0)
                    .playOn(binding!!.tvTimer);
                binding!!.tvTimer.text = "00:00"
                /*           binding!!.tvTimer.isEnabled = true
                           binding!!.tvTimer.alpha = .9f*/
            }
        }
        (cTimer as CountDownTimer).start()
    }


    private fun verifyOTP()
    {
        binding?.pbOtp?.visibility = View.VISIBLE



        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)

            .addFormDataPart("otp", binding?.etPinText?.text.toString())
            .addFormDataPart("reference_code", PreferenceUtils.getString(OTP_REF_CODE))
            .addFormDataPart("type", VERIFY_TYPE_ACCOUNT)
            .build()


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.verifyOTP(requestBody).enqueue(object: retrofit2.Callback<VerifyOTPResponse> {
                override fun onResponse(
                    call: Call<VerifyOTPResponse>,
                    response: Response<VerifyOTPResponse>
                )
                {
                    activity?.runOnUiThread {
                        binding?.pbOtp?.visibility = View.GONE
                    }


                    try {
/*                    loginProg.visibility=View.GONE
                    loginBtn.isEnabled=true*/

                        val verifyResponse: VerifyOTPResponse? =response.body()
                        val statuscode= verifyResponse!!.status
                        if (statuscode==1) {

                            Log.d("response",verifyResponse.message)

                            if (VERIFY_TYPE_ACCOUNT == TYPE_PASSWORD_RESET)
                            {
                                MainActivity.navController.navigate(R.id.resetPassFragment3)

                              //  MainActivity.navController.navigate(R.id.resetPassFragment)


                              /*  if (sharedPreferenceManager
                                        ?.getString(SELECT_USER) == "Doctor"
                                ) {
                                    MainActivity.navController.navigate(R.id.resetPassFragment)

                                } else
                                {
                                    MainActivity.navController.navigate(R.id.resetPassFragment)
                                  //  MainActivity!!.navigate(R.id.resetPassFragment2)
                                  }*/

                            }
                            else
                            {
                                val gson = Gson()
                                val json:String = gson.toJson(verifyResponse.data )
                                PreferenceUtils.saveString(USER_OBJECT,json)
                                sessionManager.saveAuthToken(verifyResponse.bearer_token)

                                PreferenceUtils.saveBoolean(IS_SOCIAL_LOGIN,false)
                                if (sharedPreferenceManager
                                        ?.getString(SELECT_USER) == "Doctor"
                                ) {
                                    MainActivity.navController.navigate(R.id.homeActivity)

                                } else
                                {
                                        MainActivity.navController.navigate(R.id.homeActivityPateint)
                                }
                            }

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

                override fun onFailure(call: Call<VerifyOTPResponse>, t: Throwable)
                {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })

        }


    }


    private fun resendOTP()
    {

        startTimer()

        binding?.pbOtp?.visibility = View.VISIBLE

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)

            .addFormDataPart("email", PreferenceUtils.getString(EMAIL))
            .build()


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.resendOTP(requestBody).enqueue(object: retrofit2.Callback<ResendOTPResponse> {
                override fun onResponse(
                    call: Call<ResendOTPResponse>,
                    response: Response<ResendOTPResponse>
                )
                {

                    activity?.runOnUiThread {
                        binding?.pbOtp?.visibility = View.GONE
                    }


                    try {
/*                    loginProg.visibility=View.GONE
                    loginBtn.isEnabled=true*/

                        response.body()?.let { Log.d("response", it.message) }
                        val resendOTPResponse: ResendOTPResponse? =response.body()
                        val statuscode= resendOTPResponse!!.status
                        if (statuscode==1) {

                            Log.d("response", resendOTPResponse.message)

                            startTimer()

                        }
                        else {
                            Toast.makeText(context, resendOTPResponse.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    catch (e:Exception)
                    {
                        //Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResendOTPResponse>, t: Throwable)
                {
                    activity?.runOnUiThread {
                        binding?.pbOtp?.visibility = View.GONE
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                }
            })

        }


    }

    override fun onDestroyView() {
        cTimer?.cancel()
        super.onDestroyView()
    }


///*
//    internal inner class WebResponseObserver : Observer<WebResponse<Any>> {
//        override fun onChanged(webResponse: WebResponse<Any>) {
//            val userModel: UserModel = getGson()!!.fromJson(
//                getGson()!!.toJson(webResponse.result),
//                UserModel::class.java
//            )
//
//            when (nameFrag) {
//                "Forget" -> {
//                    val userModelWrapper: UserModel = getGson()!!.fromJson(
//                        getGson()!!.toJson(webResponse.result),
//                        UserModel::class.java
//                    )
//                    sharedPreferenceManager?.putValue(
//                        KEY_TOKEN,
//                        webResponse.bearer_token
//                    )
//                    sharedPreferenceManager?.putValue(
//                        LOGINSIMPLEORSOCIAL,
//                        ""
//                    )
//                    sharedPreferenceManager?.putObject(KEY_CURRENT_USER_MODEL, userModelWrapper)
//                    sharedPreferenceManager?.putValue(
//                        KEY_CURRENT_USER_ID,
//                        userModelWrapper.userId.toString()
//                    )
//                    getBaseActivity().finish()
//                    navController.navigate(R.id.homeActivity)
//
//                }
//                else -> {
//                    val bundle = Bundle()
//                    bundle.putInt("user_id", userModel.userId)
//                    bundle.putString("token", webResponse.bearer_token)
//                    bundle.putString("fragmentName", "CompleteProfile")
//                    navController.popBackStack(R.id.verificationFragment, true)
//                    navController.navigate(R.id.completeProfileFragment, bundle)
//                }
//            }
//        }
//    }
//*/
}