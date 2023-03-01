package com.fictivestudios.docsvisor.fragment

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
import androidx.databinding.DataBindingUtil
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.adapter.CertificateAdapter3
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.Certification
import com.fictivestudios.docsvisor.apiManager.response.CommonResponse
import com.fictivestudios.docsvisor.apiManager.response.UpdateResponse
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.callbacks.OnSpinnerOKPressedListener
import com.fictivestudios.docsvisor.constants.Constants
import com.fictivestudios.docsvisor.databinding.DoctorProfileEditBinding
import com.fictivestudios.docsvisor.helper.*
import com.fictivestudios.docsvisor.model.SpinnerModel
import com.fictivestudios.docsvisor.model.User
import com.fictivestudios.docsvisor.widget.TitleBar
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList

class DoctorEditProfileFragment : BaseFragment(),DatePickerDialog.OnDateSetListener,
    OnItemClickListener {
    lateinit var name: String
    private var fileTemporaryProfilePicture: File? = null
    var selectedImageAdapter: CertificateAdapter3? = null
  //  var selectedImageList: ArrayList<String>? = null
    var imagesToUpload  :ArrayList<Uri>? = ArrayList<Uri>()
    var imageStringArray:ArrayList<String> = ArrayList<String>()
    var certificatesList=ArrayList<Certification>()

    var isLoading=false

    lateinit var binding:DoctorProfileEditBinding

    private var mYear:String? =null
    private var mMonth:String? =null
    private var mDayOfMonth:String? =null

    private var userObj : User?=null

    var PICK_IMAGE_MULTIPLE = 1

    var imagesPathList: MutableList<String> = arrayListOf()

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showBackButton(activity, "Edit Doctor Profile")
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

            if (!isLoading)
            {
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



        }

        binding.btnSignUp.setOnClickListener {
          //  validateFields()
            validateFields()
        }

    }

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
                            imageStringArray?.add(imageUri.toString())
                            imagesToUpload?.add(imageUri)
                            selectedImageAdapter?.notifyItemChanged(i)
                        }

/*                        selectedImageAdapter = CertificateAdapter3(activity, selectedImageList!!, this@DoctorEditProfileFragment)
                        binding.rvCertificates.adapter = selectedImageAdapter
                        binding.rvCertificates.adapter?.notifyDataSetChanged()*/
                        selectedImageAdapter?.notifyDataSetChanged()

                    }
                } else if (data?.getData() != null) {
                    var imageUri: Uri? = data.data
                    if (imageUri != null) {
                        imageStringArray?.add(imageUri.toString())
                        imagesToUpload?.add(imageUri)
                      //  selectedImageAdapter = CertificateAdapter3(activity, imageStringArray!!, this@DoctorEditProfileFragment)
                        //binding.rvCertificates.adapter = selectedImageAdapter
                        selectedImageAdapter?.notifyDataSetChanged()
                        imageStringArray?.let { selectedImageAdapter?.notifyItemChanged(it.size) }


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


    fun prepareFilePart(partName: String, file: File): MultipartBody.Part {
        val requestFile = RequestBody.create("image/png".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        mYear = year.toString()
        mMonth = String.format("%02d", month+1)
        mDayOfMonth = String.format("%02d", dayOfMonth)

        binding.txtCalender.setText("$mYear-$mMonth-$mDayOfMonth")
     //   binding.txtAge.setText(getAge(year, month, dayOfMonth).toString())
    }

    private fun openImagePicker()
    {    ImagePicker.with(this)
        .crop()
        .compress(1024)
        .maxResultSize(1080, 1080)
        .start()

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.doctor_profile_edit, container, false)
        imageStringArray = ArrayList()
        getUser()
        return binding.root


    }


    private fun updateDoctorEditProfile()
    {
        binding.pbEdit.visibility=View.VISIBLE
        binding.btnSignUp.isEnabled=false
        binding.btnSignUp.text=""

        var token = PreferenceUtils.getString(FCM,"")

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())



        val signupHM = HashMap<String, RequestBody>().apply {
            this["name"] = binding.txtFullName.text.toString().getFormDataBody()
            this["email"] = binding.txtEmail.text.toString().getFormDataBody()
            this["date_of_birth"] = binding.txtCalender.text.toString().getFormDataBody()
           // this["role"] = USER_TYPE_DOCTOR.getFormDataBody()
            this["profession"] = binding.txtProfession.text.toString().getFormDataBody()
            this["bio"] = binding.txtBio.text.toString().getFormDataBody()
            this["phone_no"] = binding.txtPhone.text.toString().getFormDataBody()
            this["country_code"] = binding.ccp.selectedCountryCode.toString().getFormDataBody()
            this["gender"] = binding.txtGender.text.toString().toLowerCase().getFormDataBody()
          //  this["token"] =token.toString().getFormDataBody()
        }

        var part: MultipartBody.Part? = null
        if (fileTemporaryProfilePicture != null){
            part = fileTemporaryProfilePicture?.getPartMap("image")
        }

        val listOfImages = ArrayList<MultipartBody.Part>()
        if (imagesToUpload != null)
        {
            for (i in 0 until imagesToUpload?.size!!) {


                listOfImages.add(prepareFilePart("certifications[$i]", /*File(imageStringArray!![i].path!!)*/
                    File( getMediaFilePathFor(imagesToUpload!![i],requireContext()))
                ))
            }
        }




        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.updateDoctorProfile(signupHM,part,listOfImages).enqueue(object: retrofit2.Callback<UpdateResponse> {
                override fun onResponse(
                    call: Call<UpdateResponse>,
                    response: Response<UpdateResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbEdit.visibility=View.GONE
                        binding.btnSignUp.isEnabled=true
                        binding.btnSignUp.text="update"

                    })


                    try {

                        Log.d("response", response.body()?.message ?: "null")

                        val response: UpdateResponse? =response.body()
                        val statuscode= response!!.status
                        if (statuscode==1) {




                            Log.d("response",response.message)

                         //   PreferenceUtils.saveString(EMAIL,binding.txtEmail.text.toString())


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

                override fun onFailure(call: Call<UpdateResponse>, t: Throwable)
                {

                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbEdit.visibility=View.GONE
                        binding.btnSignUp.isEnabled=true
                        binding.btnSignUp.text="update"
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                        Log.d("response", t.localizedMessage)
                    })

                }
            })

        }


    }


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

    override fun onItemClick(position: Int, `object`: Any?, view: View?, adapterName: String?) {

        if (!isLoading)
        {
            if (position > certificatesList?.size-1 )
            {
                imageStringArray?.removeAt(position)
                selectedImageAdapter = imageStringArray?.let { CertificateAdapter3(activity, it, this) }
                binding.rvCertificates.adapter = selectedImageAdapter
                selectedImageAdapter?.notifyItemRemoved(position)
            }
            else
            {

                deleteCertificate(certificatesList[position].id.toString(),position)
            }

        }



    }

    private fun deleteCertificate(certificateID: String, position: Int) {

        isLoading = true
        binding.pbEdit.visibility=View.VISIBLE

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("certificate_id", certificateID)
            .build()

        GlobalScope.launch(Dispatchers.IO)
        {
            apiClient.deleteCertificate(requestBody).enqueue(object: retrofit2.Callback<CommonResponse> {
                override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        isLoading = false
                        binding.pbEdit.visibility=View.GONE

                    })

                    response?.body()?.message?.let { Log.d("Response", it) }

                    try {


                        if (response.isSuccessful) {

                            if (response.body()?.status == 1) {
                                activity?.runOnUiThread(java.lang.Runnable {

                                    imageStringArray?.removeAt(position)
                                    certificatesList?.removeAt(position)
                                    selectedImageAdapter = imageStringArray?.let { CertificateAdapter3(activity, it, this@DoctorEditProfileFragment) }
                                    binding.rvCertificates.adapter = selectedImageAdapter
                                    selectedImageAdapter?.notifyItemRemoved(position)

                                    Toast.makeText(requireContext(), response?.body()?.message, Toast.LENGTH_SHORT).show()

                                })
                            }


                        }
                        else {
                            activity?.runOnUiThread(java.lang.Runnable {

                                Toast.makeText(requireContext(), response.message(), Toast.LENGTH_SHORT).show()

                            })
                        }
                    }
                    catch (e:Exception)
                    {

                        activity?.runOnUiThread(java.lang.Runnable {

                            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()

                        })
                    }
                }

                override fun onFailure(call: Call<CommonResponse>, t: Throwable)
                {

                    activity?.runOnUiThread(java.lang.Runnable {

                        isLoading = false
                        binding.pbEdit.visibility=View.GONE
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()


                    })
                }
            })

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





        if (
            binding.txtGender.text.toString().isNullOrBlank()
        )
        {
            binding.txtGender.setError(getString(R.string.fields_cant_be_empty))

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
            binding.txtPhone.setError("invalid phone format")

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
            updateDoctorEditProfile()



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

            binding.txtBio.setText(user?.bio)
            binding.txtProfession.setText(user?.profession)


            if (!user?.image.isNullOrEmpty())
            {
                Picasso.get().load(user?.image).into(binding.img)
                binding.imgCamera.visibility = View.GONE
            }



            if (user.certifications!=null)
            {
                certificatesList = user.certifications as ArrayList<Certification>
                for (i in user.certifications)
                {
                    imageStringArray.add(i.filename)
                }

                selectedImageAdapter = CertificateAdapter3(activity, imageStringArray, this@DoctorEditProfileFragment)
                binding.rvCertificates.adapter = selectedImageAdapter
                binding.rvCertificates.adapter?.notifyDataSetChanged()


            }
            //binding.txtAge.setText( PreferenceUtils.getString(AGE))
        }


    }

}