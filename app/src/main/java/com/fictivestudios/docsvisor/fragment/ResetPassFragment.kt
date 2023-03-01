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
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.ChangePassword
import com.fictivestudios.docsvisor.constants.SELECT_USER
import com.fictivestudios.docsvisor.databinding.FragmentChangePasswordBinding
import com.fictivestudios.docsvisor.databinding.FragmentResetPasswordBinding
import com.fictivestudios.docsvisor.helper.EMAIL
import com.fictivestudios.docsvisor.helper.OTP
import com.fictivestudios.docsvisor.helper.OTP_REF_CODE
import com.fictivestudios.docsvisor.helper.PreferenceUtils
import com.fictivestudios.docsvisor.helper.Validations.Companion.isValidPassword
import com.fictivestudios.docsvisor.widget.TitleBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class ResetPassFragment : BaseFragment() {

    lateinit var binding: FragmentResetPasswordBinding
    lateinit var name: String

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_reset_password, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.GONE
       // titleBar?.showBackButton( activity,"RESET PASSWORD")
    }

    override fun setListeners() {


        binding.btnSignUp.setOnClickListener {

            if (binding.txtPass.text.toString()==binding.txtConfirmPass.text.toString())
            {

                if (binding.txtPass.text.toString().isValidPassword() )
                {

                    resetPassRequest()
                }
                else
                {
                    binding.txtPass.setError(getString(R.string.password_must_be))
                    Toast.makeText(requireContext(), "Password should be of 8 characters long (should contain uppercase, lowercase, number and special character", Toast.LENGTH_LONG).show()
                }



            }
            else{
                Toast.makeText(requireContext(), "new password & confirm password doesn't match", Toast.LENGTH_SHORT).show()
            }

        }
    }



    private fun resetPassRequest()
    {

        binding.pbChangePass.visibility=View.VISIBLE
        binding.btnSignUp.isEnabled=false
        binding.btnSignUp.text=""

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)

            .addFormDataPart("otp", PreferenceUtils.getString(OTP))
            .addFormDataPart("new_password", binding.txtConfirmPass.text.toString())
            .addFormDataPart("email", PreferenceUtils.getString(EMAIL))
            .addFormDataPart("reference_code", PreferenceUtils.getString(OTP_REF_CODE))

            .build()


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.resetPassword(requestBody).enqueue(object: retrofit2.Callback<ChangePassword> {
                override fun onResponse(
                    call: Call<ChangePassword>,
                    response: Response<ChangePassword>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbChangePass.visibility=View.GONE
                        binding.btnSignUp.isEnabled=true
                        binding.btnSignUp.text="RESET"

                    })


                    try {

                        val response: ChangePassword? =response.body()
                        val statuscode= response!!.status
                        if (statuscode==1) {

                            Log.d("response", response.message)
                          //  activity?.onBackPressed()
                            if (sharedPreferenceManager
                                    ?.getString(SELECT_USER) == "Doctor"
                            ) {
                              //  activity?.onBackPressed()
                                MainActivity.navController.navigate(R.id.loginFragment)

                            } else
                            {
                               // activity?.onBackPressed()

                                MainActivity.navController.navigate(R.id.loginFragment)
                            }

                        }
                        else {
                            Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show()
                        }
                    }
                    catch (e:Exception)
                    {
                        //Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ChangePassword>, t: Throwable)
                {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbChangePass.visibility=View.GONE
                        binding.btnSignUp.isEnabled=true
                        binding.btnSignUp.text="RESET"

                    })
                }
            })

        }


    }




/*    private fun changePass()
    {
        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)

            .addFormDataPart("email", "binding.txtEmail.text.toString()")
            .build()


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.forgetPassword(requestBody).enqueue(object: retrofit2.Callback<ForgetPasswordResponse> {
                override fun onResponse(
                    call: Call<ForgetPasswordResponse>,
                    response: Response<ForgetPasswordResponse>
                )
                {
                    try {
*//*                    loginProg.visibility=View.GONE
                    loginBtn.isEnabled=true*//*

                        val resendOTPResponse: ForgetPasswordResponse? =response.body()
                        val statuscode= resendOTPResponse!!.status
                        if (statuscode==1) {

                            Log.d("response", resendOTPResponse.message)
                            MainActivity.navController.navigate(R.id.verificationFragment)

                        }
                        else {
                            Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show()
                        }
                    }
                    catch (e:Exception)
                    {
                        //Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ForgetPasswordResponse>, t: Throwable)
                {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })

        }


    }*/


    companion object {

        fun newInstance(): ResetPassFragment {

            val args = Bundle()
            val fragment = ResetPassFragment()
            fragment.arguments = args
            return fragment
        }
    }


}