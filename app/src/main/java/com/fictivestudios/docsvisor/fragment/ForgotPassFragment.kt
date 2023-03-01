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
import com.fictivestudios.docsvisor.apiManager.response.ForgetPasswordResponse
import com.fictivestudios.docsvisor.databinding.FragmentForgotPassBinding
import com.fictivestudios.docsvisor.helper.*
import com.fictivestudios.docsvisor.widget.TitleBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class ForgotPassFragment : BaseFragment() {

    lateinit var binding: FragmentForgotPassBinding
    lateinit var name: String
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
            DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_pass, container, false);

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showBackButton(activity, "FORGOT PASSWORD")
    }

    override fun setListeners() {



        binding.btnReset.setOnClickListener {

            if (!Validations.isValidEmail(binding.txtEmail.text.toString()))
            {
                binding.txtEmail.setError(getString(R.string.invalid_email_format))
            }
            else
            {
                forgetPass()
            }


        }
    }


    private fun forgetPass()
    {
        binding.pbForget.visibility=View.VISIBLE
        binding.btnReset.isEnabled=false
        binding.btnReset.text=""


        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("email", binding.txtEmail.text.toString())
            .build()


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.forgetPassword(requestBody).enqueue(object: retrofit2.Callback<ForgetPasswordResponse> {
                override fun onResponse(
                    call: Call<ForgetPasswordResponse>,
                    response: Response<ForgetPasswordResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbForget.visibility=View.GONE
                        binding.btnReset.isEnabled=true
                        binding.btnReset.text="GET CODE"

                    })

                    try {
/*                    loginProg.visibility=View.GONE
                    loginBtn.isEnabled=true*/

                        val forgetPasswordResponse: ForgetPasswordResponse? =response.body()
                        val statuscode= forgetPasswordResponse!!.status
                        if (statuscode==1) {


                            Log.d("response", forgetPasswordResponse.message)
                            VERIFY_TYPE_ACCOUNT = TYPE_PASSWORD_RESET
                            PreferenceUtils.saveString(EMAIL,binding.txtEmail.text.toString())
                            PreferenceUtils.saveString(OTP_REF_CODE,forgetPasswordResponse.data.reference_code.toString())
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

                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbForget.visibility=View.GONE
                        binding.btnReset.isEnabled=true
                        binding.btnReset.text="GET CODE"
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()

                    })
                }
            })

        }


    }


    companion object {

        fun newInstance(): ForgotPassFragment {

            val args = Bundle()
            val fragment = ForgotPassFragment()
            fragment.arguments = args
            return fragment
        }
    }


}