package com.fictivestudios.docsvisor.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.ChangePassword
import com.fictivestudios.docsvisor.databinding.FragmentChangePasswordBinding
import com.fictivestudios.docsvisor.helper.Validations.Companion.isValidPassword
import com.fictivestudios.docsvisor.widget.TitleBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class ChangePassFragment : BaseFragment() {

    lateinit var binding: FragmentChangePasswordBinding
    lateinit var name: String

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false);
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

        binding.btnLeft2.setOnClickListener {
            getBaseActivity().onBackPressed()
        }

        binding.btnSignUp.setOnClickListener {

            if (binding.txtPass.text.toString()==binding.txtConfirmPass.text.toString())
            {
                if (binding.txtPass.text.toString().isValidPassword() )
                {

                    changePassRequest()
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



    private fun changePassRequest()
    {

        binding.pbChangePass.visibility=View.VISIBLE
        binding.btnSignUp.isEnabled=false
        binding.btnSignUp.text=""

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)

            .addFormDataPart("old_password", binding.txtCurrentPass.text.toString())
            .addFormDataPart("new_password", binding.txtConfirmPass.text.toString())
            .build()


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.changePassword(requestBody).enqueue(object: retrofit2.Callback<ChangePassword> {
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
                            Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                            activity?.onBackPressed()

                        }
                        else {
                            activity?.runOnUiThread(java.lang.Runnable {

                                Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                            })
                        }
                    }
                    catch (e:Exception)
                    {
                        activity?.runOnUiThread(java.lang.Runnable {
                            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
                        })
                    }
                }

                override fun onFailure(call: Call<ChangePassword>, t: Throwable)
                {

                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbChangePass.visibility=View.GONE
                        binding.btnSignUp.isEnabled=true
                        binding.btnSignUp.text="RESET"
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
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

        fun newInstance(): ChangePassFragment {

            val args = Bundle()
            val fragment = ChangePassFragment()
            fragment.arguments = args
            return fragment
        }
    }


}