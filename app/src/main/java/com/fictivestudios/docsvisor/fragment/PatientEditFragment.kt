package com.fictivestudios.docsvisor.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import br.com.ilhasoft.support.validation.Validator
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.UpdateResponse
import com.fictivestudios.docsvisor.databinding.FragmentEditProfileBinding
import com.fictivestudios.docsvisor.helper.*
import com.fictivestudios.docsvisor.model.User
import com.fictivestudios.docsvisor.widget.TitleBar
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.squareup.picasso.Picasso
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.coroutines.*

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.time.LocalDate
import java.time.Period
import java.util.*

class PatientEditFragment : BaseFragment(), DatePickerDialog.OnDateSetListener {

    lateinit var binding: FragmentEditProfileBinding
    lateinit var name: String
    private var fileTemporaryProfilePicture: File? = null
    var validator: Validator? = null
    private var userObj : User?=null
    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false);

        getUser()

        return binding.root
    }


    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.GONE
        titleBar?.setTitle("EDIT PROFILE")
    }

    override fun setListeners() {

        btnLeft2.setOnClickListener {
            getBaseActivity().onBackPressed()
        }



        val c: Calendar = Calendar.getInstance()
        val year: Int = c.get(Calendar.YEAR)
        val month: Int = c.get(Calendar.MONTH)
        val day: Int = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            requireContext(), this, year, month, day
        )
        binding.txtCalender.setOnClickListener {
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        binding?.imgCamera?.setOnClickListener {
//            cropImagePicker(requireContext(), this)

            openImagePicker()
        }

        binding.editImage.setOnClickListener {
            openImagePicker()
        }
        binding.btnSignUp.setOnClickListener {

            validateFields()
        }
    }

    companion object {

        fun newInstance(): PatientEditFragment {

            val args = Bundle()
            val fragment = PatientEditFragment()
            fragment.arguments = args
            return fragment
        }
    }



    private fun validateFields() {


        /*if (!Validations.isValidEmail(binding.txtEmail.text.toString()))
        {
            binding.txtEmail.setError(getString(R.string.invalid_email_format))
            return
        }*/

         if (
            binding.txtPhone.text.toString().isNullOrBlank()
        )
        {
            binding.txtPhone.setError(getString(R.string.fields_cant_be_empty))

            return
        }

        if (
            binding.txtFullName.text.toString().isNullOrBlank()
        )
        {
            binding.txtFullName.setError(getString(R.string.fields_cant_be_empty))

            return
        }


   /*     if (
            binding.txtAge.text.toString().isNullOrBlank()
        )
        {
            binding.txtCalender.setError(getString(R.string.fields_cant_be_empty))

            return
        }*/




        else
        {
/*            Constants.isAccountLogin = true

            showAgeVerifyDialog()*/
                updateProfileRequest()



        }
    }


    private fun getUser()
    {

       var task = GlobalScope.async(Dispatchers.IO) {
            var gson =  Gson()
            var json:String= PreferenceUtils.getString(USER_OBJECT)
            userObj=gson.fromJson(json, User::class.java)

        }
        GlobalScope.launch(Dispatchers.Main) {
            task.await()
            userObj?.let { setUser(it) }
        }

    }

    private fun setUser(user: User) {


        if (user!=null)
        {
            binding.txtFullName.setText(user?.name)
            binding.txtEmail .setText(user?.email)
            binding.txtCalender .setText(user?.date_of_birth)
            binding.txtPhone.setText(user?.phone_no)
            if (!user?.image.isNullOrEmpty())
            {
                Picasso.get().load(user?.image).into(binding.img)
                binding.imgCamera.visibility = View.GONE
            }


            binding.txtAge.setText( PreferenceUtils.getString(AGE))
        }


    }

    private fun updateProfileRequest()
    {
        binding.pbEdit.visibility=View.VISIBLE
        binding.btnSignUp.isEnabled=false
        binding.btnSignUp.text=""


        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())



        var part : MultipartBody.Part? = null
        var updateHM = HashMap<String, RequestBody>().apply {
            this["name"] = binding.txtFullName.text.toString().getFormDataBody()
           this["email"] = binding.txtEmail.text.toString().getFormDataBody()
            this["date_of_birth"] = binding.txtCalender.text.toString().getFormDataBody()
          //  this["role"] = "doctor".toString().getFormDataBody()
           // this["profession"] = binding.txtFullName.text.toString().getFormDataBody()
            this["phone_no"] = binding.txtPhone.text.toString().getFormDataBody()
            this["country_code"] = binding.ccp.selectedCountryCode.toString().getFormDataBody()
        }
        if (fileTemporaryProfilePicture != null){
            part = fileTemporaryProfilePicture?.getPartMap("image")
        }

        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.updateProfile(updateHM,part).enqueue(object: retrofit2.Callback<UpdateResponse> {
                override fun onResponse(
                    call: Call<UpdateResponse>,
                    response: Response<UpdateResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbEdit.visibility=View.GONE
                        binding.btnSignUp.isEnabled=true
                        binding.btnSignUp.text="UPDATE"

                    })

                    try {


                        val response: UpdateResponse? =response.body()
                        val statuscode= response!!.status
                        if (statuscode==1) {

                            Log.d("response", response.message)



                            val gson = Gson()
                            val json:String = gson.toJson(response.data )
                            PreferenceUtils.saveString(USER_OBJECT,json)
                            setUser(response.data)
                            activity?.runOnUiThread(java.lang.Runnable {
                                Toast.makeText(context,"Profile Updated Successfully", Toast.LENGTH_SHORT).show()


                            })

                            activity?.onBackPressed()

                        }
                        else {


                            activity?.runOnUiThread(java.lang.Runnable {
                                Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                                Log.d("response", response.message)

                            })
                        }
                    }
                    catch (e:Exception)
                    {

                        Log.d("response", e.localizedMessage)
                        activity?.runOnUiThread(java.lang.Runnable {
                            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
                        })
                    }
                }

                override fun onFailure(call: Call<UpdateResponse>, t: Throwable)
                {

                    activity?.runOnUiThread(java.lang.Runnable {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                        Log.d("response", t.localizedMessage)

                        binding.pbEdit.visibility=View.GONE
                        binding.btnSignUp.isEnabled=true
                        binding.btnSignUp.text="UPDATE"

                    })
                }
            })

        }


    }



    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        var mYear = year.toString()
        var mMonth = String.format("%02d", month)
        var mDay = String.format("%02d", dayOfMonth)
        binding.txtCalender.text = "$mYear-$mMonth-$mDay"
        binding.txtAge.setText(getAge(year, month, dayOfMonth).toString())
        PreferenceUtils.saveString(AGE,getAge(year, month, dayOfMonth).toString())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAge(year: Int, month: Int, dayOfMonth: Int): Int {
        return Period.between(
            LocalDate.of(year, month, dayOfMonth),
            LocalDate.now()
        ).years
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
/*
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val uri = compressImage(result.uri)//File(result.uri.path)
                fileTemporaryProfilePicture = File(uri.path!!)
                //\                uploadImageFile(fileTemporaryProfilePicture.getPath(), result.getUri().toString());
                setImageAfterResult(result.uri.toString())
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                error.printStackTrace()
            }
        }
*/


        //val result = CropImage.getActivityResult(data)

        if (resultCode == Activity.RESULT_OK) {
            val uri: Uri = data?.data!!
            //  val uri = compressImage(result.uri)//File(result.uri.path)
            fileTemporaryProfilePicture = File(uri.path!!)

            activity?.runOnUiThread {
                binding.img.setImageURI(uri)
                binding.imgCamera.visibility=View.INVISIBLE
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


    private fun openImagePicker()
    {    ImagePicker.with(this)
        .crop()	    			//Crop image(Optional), Check Customization for more option
        .compress(1024)			//Final image size will be less than 1 MB(Optional)
        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
        .start()

    }


    private fun setImageAfterResult(uploadFilePath: String) {
        requireActivity().runOnUiThread {
            try {
                ImageLoader.getInstance()
                    .init(ImageLoaderConfiguration.createDefault(activity))
                ImageLoader.getInstance().displayImage(uploadFilePath, binding?.img)
                binding.imgCamera.visibility = View.INVISIBLE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    @Throws(IOException::class)
    private fun compressImage(imageUri: Uri): Uri {
        val compressedFile: File = Compressor(context)
            .setMaxWidth(500)
            .setMaxHeight(500)
            .setQuality(75)
            .compressToFile(File(imageUri.getPath()))
        return Uri.fromFile(compressedFile)
    }


}