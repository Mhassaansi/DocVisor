package com.fictivestudios.docsvisor.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.MainActivity
import com.fictivestudios.docsvisor.adapter.CertificateAdapter
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.SignupResponse
import com.fictivestudios.docsvisor.callbacks.OnSpinnerOKPressedListener
import com.fictivestudios.docsvisor.constants.Constants
import com.fictivestudios.docsvisor.databinding.FragmentSignupPatientBinding
import com.fictivestudios.docsvisor.helper.*
import com.fictivestudios.docsvisor.helper.Validations.Companion.isValidPassword
import com.fictivestudios.docsvisor.model.SpinnerModel
import com.fictivestudios.docsvisor.widget.TitleBar
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.time.LocalDate
import java.time.Period
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignupPatientFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupPatientFragment : BaseFragment() , DatePickerDialog.OnDateSetListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var spinnerGenderDialogFragment:SpinnerDialogFragment?=null

    private var mYear:String? =null
    private var mMonth:String? =null
    private var mDayOfMonth:String? =null

    var selectedImageAdapter: CertificateAdapter? = null
    var selectedImageList: ArrayList<String>? = null
    lateinit var binding: FragmentSignupPatientBinding
    lateinit var name: String
    private var fileTemporaryProfilePicture: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
/*        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            PreferenceUtils.saveString(FCM,token.toString())
            // Log and toast



            Toast.makeText(requireContext(), task.exception.toString(), Toast.LENGTH_SHORT).show()
        })*/


    }

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showBackButton(activity, "SIGNUP")    }

    override fun setListeners() {
        val c: Calendar = Calendar.getInstance()
        val year: Int = c.get(Calendar.YEAR)
        val month: Int = c.get(Calendar.MONTH)
        val day: Int = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            requireContext(), this, year, month, day
        )
        binding.txtCalender.setOnClickListener {
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis();
            datePickerDialog.show();
        }

        binding?.imgCamera?.setOnClickListener {
            openImagePicker()
        }




         spinnerGenderDialogFragment =
            SpinnerDialogFragment.newInstance(
                "Please Select",
                Constants.arrGender,
                object :
                    OnSpinnerOKPressedListener {
                    override fun onSingleSelection(data: SpinnerModel, selectedPosition: Int) {

                        binding.txtGender.setText(data.text)
                    }
                },
                0
            )
        binding.txtGender.setOnClickListener {
            spinnerGenderDialogFragment?.show(
                requireActivity().supportFragmentManager,
                "SpinnerDialogFragmentSingle"
            )
        }



        binding.btnSignUp.setOnClickListener {
            validateFields()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_signup_patient, container, false)


        selectedImageList = ArrayList()
        return binding.root

    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        mYear = year.toString()
        mMonth = String.format("%02d", month+1)
        mDayOfMonth = String.format("%02d", dayOfMonth)

        binding.txtCalender.setText("$mYear-$mMonth-$mDayOfMonth")
        binding.txtAge.setText(getAge(year, month, dayOfMonth).toString())
    }

    fun getAge(year: Int, month: Int, dayOfMonth: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Period.between(
                LocalDate.of(year, month, dayOfMonth),
                LocalDate.now()
            ).years
        } else {
            0
        }
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {

        //val result = CropImage.getActivityResult(data)

        if (resultCode == Activity.RESULT_OK) {
            val uri: Uri = data?.data!!
            //  val uri = compressImage(result.uri)//File(result.uri.path)
            fileTemporaryProfilePicture = File(uri.path!!)

            activity?.runOnUiThread {
                binding.img.setImageURI(uri)
                binding.imgCamera.visibility=View.GONE
            }

            //\                uploadImageFile(fileTemporaryProfilePicture.getPath(), result.getUri().toString());
            // setImageAfterResult(result.uri.toString())

        }
        else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }

    }



    private fun validateFields() {

        if (
            binding.txtFullName.text.toString().isNullOrBlank()
        )
        {
            binding.txtFullName.setError(getString(R.string.fields_cant_be_empty))

            return
        }


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

        if (binding.txtPass.text.toString() != binding.txtConfirmPass.text.toString() )
        {
            binding.txtConfirmPass.setError(getString(R.string.confirm_pass_doesnt_match))

            return
        }

        if (
            binding.txtAge.text.toString().isNullOrBlank()
        )
        {
            binding.txtCalender.setError(getString(R.string.fields_cant_be_empty))

            return
        }

        if (
            binding.txtPhone.text.toString().isNullOrBlank()
        )
        {
            binding.txtPhone.setError(getString(R.string.fields_cant_be_empty))

            return
        }








        else
        {
/*            Constants.isAccountLogin = true

            showAgeVerifyDialog()*/
            signUp()



        }
    }


    private fun signUp()
    {
        binding.pbSignup.visibility=View.VISIBLE
        binding.btnSignUp.isEnabled=false
        binding.btnSignUp.text=""

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())


        var part: MultipartBody.Part? = null
        val signupHM = HashMap<String, RequestBody>().apply {
            this["name"] = binding.txtFullName.text.toString().getFormDataBody()
            this["email"] = binding.txtEmail.text.toString().getFormDataBody()
            this["password"] = binding.txtConfirmPass.text.toString().getFormDataBody()
            this["date_of_birth"] = binding.txtCalender.text.toString().getFormDataBody()
            this["role"] = /*PreferenceUtils.getString(SELECT_USER)*/"patient".getFormDataBody()
            this["phone_no"] = binding.txtPhone.text.toString().getFormDataBody()
            this["country_code"] = binding.ccp.selectedCountryCodeWithPlus.toString().getFormDataBody()
            this["gender"] = binding.txtGender.text.toString().toLowerCase().getFormDataBody()
            this["height"] = binding.txtHeight.text.toString().getFormDataBody()
            this["weight"] = binding.txtWeight.text.toString().getFormDataBody()
        }

        if (fileTemporaryProfilePicture != null){
            part = fileTemporaryProfilePicture!!.getPartMap("image")
        }

        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.signup(signupHM,part,null).enqueue(object: retrofit2.Callback<SignupResponse> {
                override fun onResponse(
                    call: Call<SignupResponse>,
                    response: Response<SignupResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbSignup.visibility=View.GONE
                        binding.btnSignUp.isEnabled=true
                        binding.btnSignUp.text="SIGNUP"

                    })


                    try {


                        val response: SignupResponse? =response.body()
                        val statuscode= response!!.status
                        if (statuscode==1) {



                            Log.d("response",response.message)

                            val gson = Gson()
                            val json:String = gson.toJson(response.data )
                            PreferenceUtils.saveString(USER_OBJECT,json)


                            PreferenceUtils.saveString(EMAIL,binding.txtEmail.text.toString())
                            PreferenceUtils.saveString(OTP_REF_CODE,response.data.reference_code.toString())
                            MainActivity.navController.navigate(R.id.verificationFragment)

                        }
                        else {
                            Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                            Log.d("signupResponse",response.message)
                        }
                    }
                    catch (e:Exception)
                    {
                        //Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<SignupResponse>, t: Throwable)
                {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbSignup.visibility=View.GONE
                        binding.btnSignUp.isEnabled=true
                        binding.btnSignUp.text="SIGNUP"

                    })

                }
            })

        }


    }


    private fun openImagePicker()
    {    ImagePicker.with(this)
        .crop()	    			//Crop image(Optional), Check Customization for more option
        .compress(1024)			//Final image size will be less than 1 MB(Optional)
        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
        .start()

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignupPatientFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignupPatientFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}