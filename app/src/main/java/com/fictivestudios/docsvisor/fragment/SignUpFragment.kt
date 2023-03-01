package com.fictivestudios.docsvisor.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
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
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.callbacks.OnSpinnerOKPressedListener
import com.fictivestudios.docsvisor.constants.Constants
import com.fictivestudios.docsvisor.databinding.FragmentSignupBinding
import com.fictivestudios.docsvisor.enums.FileType
import com.fictivestudios.docsvisor.helper.*
import com.fictivestudios.docsvisor.helper.Validations.Companion.isValidPassword
import com.fictivestudios.docsvisor.model.SpinnerModel
import com.fictivestudios.docsvisor.widget.TitleBar
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.Period
import java.util.*
import kotlin.collections.ArrayList


class SignUpFragment : BaseFragment(), DatePickerDialog.OnDateSetListener,OnItemClickListener {
    var selectedImageAdapter: CertificateAdapter? = null
    var selectedImageList: ArrayList<Uri>? = null
    lateinit var binding: FragmentSignupBinding
    lateinit var name: String
    private var fileTemporaryProfilePicture: File? = null


    private var mYear:String? =null
    private var mMonth:String? =null
    private var mDayOfMonth:String? =null


    var PICK_IMAGE_MULTIPLE = 1
     var imagePath: String?=null
    var imagesPathList: MutableList<String> = arrayListOf()

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false)

/*        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }


            // Get new FCM registration token
            val token = task.result

            PreferenceUtils.saveString(FCM,token.toString())
            // Log and toast

            Log.d("fcmtoken", token.toString())

            Toast.makeText(requireContext(), task.exception.toString(), Toast.LENGTH_SHORT).show()
        })*/





        selectedImageList = ArrayList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showBackButton(activity, "SIGNUP")
    }

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

        val spinnerSingleSelectDialogFragment =
            SpinnerDialogFragment.newInstance(
                "Please Select",
                Constants.arrSym,
                object :
                    OnSpinnerOKPressedListener {
                    override fun onSingleSelection(data: SpinnerModel, selectedPosition: Int) {
                        /*  binding!!.txtQualification.setTextColor(
                              ContextCompat.getColor(
                                  activity!!,
                                  R.color.black
                              )
                          )*/
                        binding.txtProfession.setText(data.text)
                    }
                },
                0
            )
        binding.txtProfession.setOnClickListener {
            spinnerSingleSelectDialogFragment.show(
                requireActivity().supportFragmentManager,
                "SpinnerDialogFragmentSingle"
            )
        }



        val spinnerGenderDialogFragment =
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
            spinnerGenderDialogFragment.show(
                requireActivity().supportFragmentManager,
                "SpinnerDialogFragmentSingle"
            )
        }


        binding!!.ivAddImage.setOnClickListener {
  /*          val i = Intent(activity, ImagesActivity::class.java)
            i.putExtra("name", "feeds")
            i.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(i)*/

            if (Build.VERSION.SDK_INT < 19) {
                var intent = Intent()
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(intent, "Select Picture")
                    , PICK_IMAGE_MULTIPLE
                )
            } else {
                var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "image/*"
                startActivityForResult(intent, PICK_IMAGE_MULTIPLE);
            }

        }

        binding.btnSignUp.setOnClickListener {
           validateFields()
        }

    }



    companion object {

        fun newInstance(): SignUpFragment {

            val args = Bundle()
            val fragment = SignUpFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {



        mYear = year.toString()
        mMonth = String.format("%02d", month+1)
        mDayOfMonth = String.format("%02d", dayOfMonth)

        binding.txtCalender.setText("$mYear-$mMonth-$mDayOfMonth")
        binding.txtAge.setText(getAge(year, month, dayOfMonth).toString())
        PreferenceUtils.saveString(AGE,getAge(year, month, dayOfMonth).toString())
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

  /*  override fun onAttach(context: Context) {
        super.onAttach(context)

        getMessage = activity as GetMessage
    }*/

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {

            //val result = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK) {

                if (requestCode == PICK_IMAGE_MULTIPLE)
                {
                    if (data?.getClipData() != null) {
                        var count = data.clipData?.itemCount

                        if (count != null) {
                            for (i in 0 .. count-1 ) {
                                var imageUri: Uri = data.clipData?.getItemAt(i)?.uri!!
                                selectedImageList?.add(imageUri)
                            }

                            selectedImageAdapter = CertificateAdapter(activity, selectedImageList!!, this@SignUpFragment, FileType.IMAGE)
                            binding.rvFeedback.adapter = selectedImageAdapter
                            binding.rvFeedback.adapter?.notifyDataSetChanged()

                        }
                    } else if (data?.getData() != null) {
                        var imageUri: Uri? = data.data
                        if (imageUri != null) {
                            selectedImageList?.add(imageUri)
                            selectedImageAdapter = CertificateAdapter(activity, selectedImageList!!, this@SignUpFragment, FileType.IMAGE)
                            binding.rvFeedback.adapter = selectedImageAdapter
                            binding.rvFeedback.adapter?.notifyDataSetChanged()
                        }

                    }
                }
                else
                {
                    val uri: Uri = data?.data!!
                    //  val uri = compressImage(result.uri)//File(result.uri.path)
                    fileTemporaryProfilePicture = File(uri.path!!)

                    activity?.runOnUiThread {
                        binding.img.setImageURI(uri)
                        binding.imgCamera.visibility=View.GONE
                    }
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

    private fun setImageAfterResult(uploadFilePath: String) {
        requireActivity().runOnUiThread {
            try {
                ImageLoader.getInstance()
                    .init(ImageLoaderConfiguration.createDefault(activity))
                ImageLoader.getInstance().displayImage(uploadFilePath, binding?.img)
                binding.imgCamera.visibility = View.GONE
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
            binding.txtGender.text.toString().isNullOrBlank()
        )
        {
            binding.txtGender.setError(getString(R.string.fields_cant_be_empty))

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

        if (
            binding.txtPhone.text.toString().length < 10
        )
        {
            binding.txtPhone.setError("invalid format")

            return
        }

        if (
            binding.txtHeight.text.toString().isNullOrBlank()
        )
        {
            binding.txtHeight.setError(getString(R.string.fields_cant_be_empty))

            return
        }
        if (
            binding.txtWeight.text.toString().isNullOrBlank()
        )
        {
            binding.txtWeight.setError(getString(R.string.fields_cant_be_empty))

            return
        }

        if (
            binding.txtProfession.text.toString().isNullOrBlank()
        )
        {
            binding.txtProfession.setError(getString(R.string.fields_cant_be_empty))

            return
        }


        if (
            binding.txtBio.text.toString().isNullOrBlank()
        )
        {
            binding.txtBio.setError(getString(R.string.fields_cant_be_empty))

            return
        }







        else
        {
/*            Constants.isAccountLogin = true

            showAgeVerifyDialog()*/
                signUp()



        }
    }

    fun prepareFilePart(partName: String, file: File): MultipartBody.Part {
        val requestFile = RequestBody.create("image/png".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    private fun signUp()
    {
        binding.pbSignup.visibility=View.VISIBLE
        binding.btnSignUp.isEnabled=false
        binding.btnSignUp.text=""

        var token = PreferenceUtils.getString(FCM,"")
        if (token.equals("") || token.isNullOrEmpty())
        {
             FirebaseMessaging.getInstance().token.addOnCompleteListener {
                if (it.isSuccessful)
                {
                    token = it.result
                }
            }
        }

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())



        val signupHM = HashMap<String, RequestBody>().apply {
            this["name"] = binding.txtFullName.text.toString().getFormDataBody()
            this["email"] = binding.txtEmail.text.toString().getFormDataBody()
            this["password"] = binding.txtConfirmPass.text.toString().getFormDataBody()
            this["date_of_birth"] = binding.txtCalender.text.toString().getFormDataBody()
            this["role"] = USER_TYPE_DOCTOR.getFormDataBody()
            this["profession"] = binding.txtProfession.text.toString().getFormDataBody()
            this["bio"] = binding.txtBio.text.toString().getFormDataBody()
            this["phone_no"] = binding.txtPhone.text.toString().getFormDataBody()
            this["country_code"] = binding.ccp.selectedCountryCode.toString().getFormDataBody()
            this["gender"] = binding.txtGender.text.toString().toLowerCase().getFormDataBody()
            this["height"] = binding.txtHeight.text.toString().getFormDataBody()
            this["weight"] = binding.txtWeight.text.toString().getFormDataBody()
            this["token"] =token.toString().getFormDataBody()
        }

        var part: MultipartBody.Part? = null
        if (fileTemporaryProfilePicture != null){
            part = fileTemporaryProfilePicture?.getPartMap("image")
        }

        val listOfImages = ArrayList<MultipartBody.Part>()
        if (selectedImageList != null)
        {
            for (i in 0 until selectedImageList?.size!!) {


                listOfImages.add(prepareFilePart("certifications[$i]", /*File(selectedImageList!![i].path!!)*/
               File( getMediaFilePathFor(selectedImageList!![i],requireContext()))
                ))
            }
        }




        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.signup(signupHM,part,listOfImages).enqueue(object: retrofit2.Callback<SignupResponse> {
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

                        Log.d("response", response.body()?.message ?: "null")

                        val response: SignupResponse? =response.body()
                        val statuscode= response!!.status
                        if (statuscode==1) {



                            Log.d("response",response.message)

    //                        val gson = Gson()
  //                          val json:String = gson.toJson(response.data )
//                            PreferenceUtils.saveString(USER_OBJECT,json)



                            PreferenceUtils.saveString(EMAIL,binding.txtEmail.text.toString())
                            PreferenceUtils.saveString(OTP_REF_CODE,response.data.reference_code.toString())
                            MainActivity.navController.navigate(R.id.verificationFragment)

                        }
                        else {
                            activity?.runOnUiThread(java.lang.Runnable {
                                Toast.makeText(requireContext(),response.message, Toast.LENGTH_SHORT).show()
                            })
                        }
                    }
                    catch (e:Exception)
                    {

                        activity?.runOnUiThread(java.lang.Runnable {
                            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                            Log.d("response", e.localizedMessage)
                            Log.d("response", e.message.toString())
                        })
                    }
                }

                override fun onFailure(call: Call<SignupResponse>, t: Throwable)
                {

                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbSignup.visibility=View.GONE
                        binding.btnSignUp.isEnabled=true
                        binding.btnSignUp.text="SIGNUP"
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                        Log.d("response", t.localizedMessage)
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

    /*private fun signUp()
    {
        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("email", binding.txtEmail.text.toString())
            .addFormDataPart("password", binding.txtConfirmPass.text.toString())
            .addFormDataPart("date_of_birth", *//*binding.txtCalender.text.toString()*//*"2022-01-01")
            .addFormDataPart("role", binding.txtProfession.text.toString())
            .addFormDataPart("name", *//*binding.txtFullName.text.toString()*//*"umair")
*//*            .addFormDataPart("email", param2)*//*
            .build()


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.signup(requestBody).enqueue(object: retrofit2.Callback<SignupResponse> {
                override fun onResponse(
                    call: Call<SignupResponse>,
                    response: Response<SignupResponse>
                )
                {


                    try {
*//*                    loginProg.visibility=View.GONE
                    loginBtn.isEnabled=true*//*

                        val signupResponse: SignupResponse? =response.body()
                        val statuscode= signupResponse!!.status
                        if (statuscode==1) {

                            Log.d("response",signupResponse.message)

                            PreferenceUtils.saveString(EMAIL,binding.txtEmail.text.toString())
                            PreferenceUtils.saveString(OTP_REF_CODE,signupResponse.signupData.reference_code.toString())
                            MainActivity.navController.navigate(R.id.verificationFragment)

                        } else {
                            Toast.makeText(requireContext(), signupResponse.message, Toast.LENGTH_SHORT).show()
                            Log.d("error code",signupResponse.message)
                        }
                    }
                    catch (e:Exception)
                    {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<SignupResponse>, t: Throwable)
                {
                    Toast.makeText(requireContext(), t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })

        }


    }*/

    override fun onItemClick(position: Int, `object`: Any?, view: View?, adapterName: String?) {
        selectedImageList!!.removeAt(position)
        selectedImageAdapter = CertificateAdapter(activity, selectedImageList!!, this, FileType.IMAGE)
        binding.rvFeedback.adapter = selectedImageAdapter
    }


    override fun onStart() {
        super.onStart()
      /*  LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            mMessageReceiver,
            IntentFilter("showcaseImages")
        )*/
    }

    override fun onStop() {
        super.onStop()
       // LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mMessageReceiver)
    }

/*    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            try {

                selectedImageList = intent.getStringArrayListExtra("imageList")
                Log.d("selectedImageList",selectedImageList.toString())
                selectedImageAdapter = CertificateAdapter(activity, selectedImageList!!, this@SignUpFragment, FileType.IMAGE)
                binding.rvFeedback.adapter = selectedImageAdapter
                binding.rvFeedback.adapter?.notifyDataSetChanged()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onImageList(arrayList: ArrayList<String>) {
        selectedImageList = arrayList
        Log.d("selectedImageList",selectedImageList.toString())
        selectedImageAdapter = CertificateAdapter(activity, selectedImageList!!, this@SignUpFragment, FileType.IMAGE)
        binding.rvFeedback.adapter = selectedImageAdapter
        binding.rvFeedback.adapter?.notifyDataSetChanged()
    }*/


    fun getMediaFilePathFor(
        uri: Uri,
        context: Context
    ): String {
        val returnCursor =
            context.contentResolver.query(uri, null, null, null, null)
        val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val size = returnCursor.getLong(sizeIndex).toString()
        val file = File(context.filesDir, name)
        try {
            val inputStream =
                context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            var read = 0
            val maxBufferSize = 1 * 1024 * 1024
            val bytesAvailable = inputStream!!.available()
            //int bufferSize = 1024;
            val bufferSize = bytesAvailable.coerceAtMost(maxBufferSize)
            val buffers = ByteArray(bufferSize)
            while (inputStream.read(buffers).also { read = it } != -1) {
                outputStream.write(buffers, 0, read)
            }
            Log.e("File Size %d", "" + file.length())
            inputStream.close()
            outputStream.close()
            Log.e("File Size %s", file.path)
            Log.e("File Size %d", "" + file.length())
        } catch (e: java.lang.Exception) {
            Log.e("File Size %s", e.message!!)
        }
        return file.path
    }

}