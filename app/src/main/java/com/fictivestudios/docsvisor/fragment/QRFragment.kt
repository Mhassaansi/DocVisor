package com.fictivestudios.docsvisor.fragment

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.*
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.HomeActivityPateint
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.WatchBindResponse
import com.fictivestudios.docsvisor.helper.PreferenceUtils
import com.fictivestudios.docsvisor.helper.USER_OBJECT
import com.fictivestudios.docsvisor.model.User
import com.fictivestudios.docsvisor.widget.TitleBar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.dialogue_imei.view.*
import kotlinx.android.synthetic.main.fragment_q_r.*
import kotlinx.android.synthetic.main.fragment_q_r.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [QRFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QRFragment : BaseFragment() {
    private val PERMISSION_REQUEST_CODE: Int =200

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var dialog1:Dialog
   // lateinit var dialogCalibrate:Dialog


    private lateinit var mView: View
    private lateinit var codeScanner: CodeScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar!!.setbtnrightgone()
        ///titleBar?.showSidebar(getBaseActivity(), "Patient")
        titleBar?.setTitle("WATCH CONNECTIVITY")
    }

    override fun setListeners() {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView =inflater.inflate(R.layout.fragment_q_r, container, false)


        dialog1 = Dialog(requireContext())
      //  dialogCalibrate = Dialog(requireContext())

        codeScanner()

        if (checkPermission()) {
            //main logic or main code


            // . write your main code to execute, It will execute if the permission is already given.

        } else {
            requestPermission();
        }



        mView.scanner_view.setOnClickListener {


            if (checkPermission()) {
                //main logic or main code

                codeScanner.startPreview()

                // . write your main code to execute, It will execute if the permission is already given.

            } else {
                requestPermission();
            }
        }


        mView.btn_watch.setOnClickListener {

            dialog1.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog1.setContentView(R.layout.dialogue_imei)
            dialog1.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            dialog1.show()

            val btnBind = dialog1.findViewById<androidx.appcompat.widget.LinearLayoutCompat>(R.id.btnBind)
            val etImei = dialog1.findViewById<AppCompatEditText>(R.id.et_imei)
            val btnClose = dialog1.findViewById<AppCompatImageView>(R.id.cancel)


            btnClose.setOnClickListener {
                dialog1.dismiss()
            }

            btnBind.setOnClickListener {
                checkBound(etImei.text.toString(),"bind")
            }
        }


     /*   btn_calibrate.setOnClickListener {




        }*/



        return mView
    }

    override fun onResume() {
        super.onResume()
        if (checkPermission()) {
            //main logic or main code

            codeScanner.startPreview()

            // . write your main code to execute, It will execute if the permission is already given.

        } else {
            requestPermission();
        }
    }


/*    private fun showCalibrateDialog()
    {
        dialogCalibrate.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialogCalibrate.setContentView(R.layout.dialogue_imei)
        dialogCalibrate.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialogCalibrate.show()

        val btnBind = dialogCalibrate.findViewById<androidx.appcompat.widget.LinearLayoutCompat>(R.id.btnBind)
        val etbg = dialogCalibrate.findViewById<AppCompatEditText>(R.id.et_blood_gluco)
        val lbp = dialogCalibrate.findViewById<AppCompatEditText>(R.id.et_low_blood)
        val hbp = dialogCalibrate.findViewById<AppCompatEditText>(R.id.et_high_blood)
        val bt = dialogCalibrate.findViewById<AppCompatEditText>(R.id.et_temperature)

        val btnClose = dialogCalibrate.findViewById<AppCompatImageView>(R.id.cancel)
        val btnCalibrate = dialogCalibrate.findViewById<AppCompatImageView>(R.id.btn_calibrate)




        btnCalibrate.setOnClickListener {



        }
    }*/

    override fun onPause() {

        super.onPause()
        if (checkPermission()) {
            //main logic or main code

            codeScanner.startPreview()

            // . write your main code to execute, It will execute if the permission is already given.

        } else {
            requestPermission();
        }
    }


    private fun codeScanner()
    {
        codeScanner = CodeScanner(requireActivity(), mView.scanner_view)

        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not


        if (checkPermission()) {
            //main logic or main code

            // Callbacks
            codeScanner.decodeCallback = DecodeCallback {
                activity?.runOnUiThread {
                    Toast.makeText(requireContext(), "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
                    Log.d("qrresult",it.toString())
                    checkBound(it.toString(),"bind")

                }
            }
        } else {
            requestPermission();
        }


        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            activity?.runOnUiThread {
                Toast.makeText(requireContext(), "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun checkBound(imei: String,type:String) {

        //binding.pbFitness.visibility = View.VISIBLE

        mView.pbBound.visibility=View.VISIBLE
        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)

            .addFormDataPart("imei",imei)
            .addFormDataPart("type",type)
            .build()


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.checkBound(requestBody).enqueue(object: retrofit2.Callback<WatchBindResponse> {
                override fun onResponse(
                    call: Call<WatchBindResponse>,
                    response: Response<WatchBindResponse>
                )
                {
                  /*  activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbFitness.visibility=View.GONE

                    })*/
                    mView.pbBound.visibility=View.GONE
                    Log.d("Response", response.body()?.message.toString())

                    try {


                        if (response.isSuccessful) {

                            if (response.body()?.status == 1)
                            {
                                Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()



                                val responseData: User? = response?.body()?.data

                                val gson = Gson()
                                val json:String = gson.toJson(responseData)
                                PreferenceUtils.saveString(USER_OBJECT,json)
                                dialog1.dismiss()
                                if (responseData?.watch_calibration == 0)
                                {

                                    HomeActivityPateint.navControllerPatient?.navigate(R.id.calibrateFragment)

                                }
                                else{
                                    activity?.onBackPressed()
                                }



                            }
                            else
                            {
                                Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()

                            }
                        }
                        else {
                            Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    catch (e:Exception)
                    {

                        activity?.runOnUiThread(java.lang.Runnable {
                            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()

                        })
                    }
                }

                override fun onFailure(call: Call<WatchBindResponse>, t: Throwable)
                {


                    activity?.runOnUiThread(java.lang.Runnable {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                        mView.pbBound.visibility=View.GONE
                     })
                }
            })




        }
    }



    private fun checkPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(requireContext(), "please allow camera permission", Toast.LENGTH_SHORT).show()
            requestPermission()
            // Permission is not granted
            false
        } else true
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(Manifest.permission.CAMERA),
            PERMISSION_REQUEST_CODE
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QRFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QRFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}