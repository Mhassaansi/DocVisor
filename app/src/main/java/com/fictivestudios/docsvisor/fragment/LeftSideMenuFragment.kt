package com.fictivestudios.docsvisor.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.HomeActivity
import com.fictivestudios.docsvisor.activities.HomeActivity.Companion.navControllerHome
import com.fictivestudios.docsvisor.activities.HomeActivityPateint
import com.fictivestudios.docsvisor.activities.HomeActivityPateint.Companion.navControllerPatient
import com.fictivestudios.docsvisor.activities.MainActivity
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.*
import com.fictivestudios.docsvisor.constants.*
import com.fictivestudios.docsvisor.databinding.FragmentSidemenuBinding
import com.fictivestudios.docsvisor.helper.*
import com.fictivestudios.docsvisor.model.User
import com.fictivestudios.docsvisor.widget.TitleBar

import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class LeftSideMenuFragment : BaseFragment() {

    private var userObj :User?=null
    private var binding: FragmentSidemenuBinding? = null

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sidemenu, container, false);



        leftSideMenuFragment = this
        getUser()



        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        if (sharedPreferenceManager?.getString(LOGINSIMPLEORSOCIAL) == SOCIALLOGIN) {
            //    binding!!.txtChangePass.visibility = View.GONE
        }

        if (sharedPreferenceManager?.getString(SELECT_USER).toString() == "Doctor") {
            binding!!.doctorLeftSideMenu.visibility = View.VISIBLE
        } else {
            binding!!.patientLeftSideMenu.visibility = View.VISIBLE

        }

        binding!!.doctortxtAppointment.setOnClickListener {
            navControllerHome.navigate(R.id.appointmentTaB)

            activity?.onBackPressed()
        }
        /*   binding!!.txtName.text = getCurrentUser()?.userName
           binding!!.txtEmail.text = getCurrentUser()?.userEmail
           if (getCurrentUser()?.userImage != null)
               ImageLoaderHelper.loadImageWithoutAnimation(
                   binding!!.profilePic,
                   getCurrentUser()?.userImage,
                   false
               )*/
        // Picasso.get().load(getCurrentUser()?.userImage).into(binding!!.profilePic)

    }

    override fun setTitlebar(titleBar: TitleBar?) {
        /*  titleBar?.visibility = View.VISIBLE
          titleBar?.showBackButton(activity,"Profile")*/
    }



    private fun getUser()
    {


            var gson =  Gson()
            var json:String= PreferenceUtils.getString(USER_OBJECT)
            userObj=gson.fromJson(json, User::class.java)

            setUser()




    }
    private fun setUser() {



        if (userObj!=null)
        {
            binding?.txtName?.setText(userObj?.name)
            binding?.txtEmail?.setText(userObj?.email)
            if (!userObj?.image.isNullOrBlank())
            {
                Picasso.get().load(userObj?.image).placeholder(R.drawable.usersidemenue) .into(binding?.img)
            }

        }

        if (PreferenceUtils.getBoolean(IS_SOCIAL_LOGIN))
        {
            binding?.imgLastActivityUser?.visibility = View.GONE
        }
        else
        {
            binding?.imgLastActivityUser?.visibility = View.VISIBLE
        }


    }

    private fun getUserForWatch()
    {


            var gson =  Gson()
            var json:String= PreferenceUtils.getString(USER_OBJECT)
            userObj=gson.fromJson(json, User::class.java)



    }

    override fun setListeners() {
        binding!!.txtHome.setOnClickListener {
            if (sharedPreferenceManager?.getString(DOCTOR_BOTTOMNAV).toString() == "Home") {
                getBaseActivity().onBackPressed()
            } else {

                if (sharedPreferenceManager?.getString(DOCTOR_BOTTOMNAV).toString() == "Profile") {
                    navControllerHome.popBackStack(R.id.doctorPatientFragment, true)
                }
                sharedPreferenceManager?.putValue(
                    DOCTOR_BOTTOMNAV,
                    "Home"
                )
                navControllerHome.navigate(R.id.doctorPatientFragment)
            }

        }

        binding?.doctor?.setOnClickListener {
            navControllerPatient!!.navigate(R.id.doctorThirdPartyFragment)
        }
        binding!!.patienttxtHome.setOnClickListener {
            if (sharedPreferenceManager?.getString(DOCTOR_BOTTOMNAV).toString() == "HomePatient") {
                getBaseActivity().onBackPressed()
            } else {

                when (sharedPreferenceManager?.getString(DOCTOR_BOTTOMNAV)
                    .toString()) {
                    "HistoryPatient" -> {
                        navControllerPatient!!.popBackStack(R.id.patientHistoryFragment, true)
                    }
                    "ProfilePatient" -> {
                        navControllerPatient!!.popBackStack(R.id.patientProfileFragment, true)
                    }
                    "FitnessPatient" -> {
                        navControllerPatient!!.popBackStack(R.id.patientFitnessFragment, true)
                    }
                }
                sharedPreferenceManager?.putValue(
                    DOCTOR_BOTTOMNAV,
                    "HomePatient"
                )
                navControllerPatient!!.navigate(R.id.patientHomeFragment)
            }

        }

        binding!!.cancel.setOnClickListener {
            getBaseActivity().onBackPressed()
        }

        binding!!.txtProfile.setOnClickListener {
            if (sharedPreferenceManager?.getString(DOCTOR_BOTTOMNAV).toString() == "Profile") {
                getBaseActivity().onBackPressed()
            } else {
                if (sharedPreferenceManager?.getString(DOCTOR_BOTTOMNAV).toString() == "Home") {
                    navControllerHome.popBackStack(R.id.doctorPatientFragment, true)
                }
                sharedPreferenceManager?.putValue(
                    DOCTOR_BOTTOMNAV,
                    "Profile"
                )
                navControllerHome.navigate(R.id.doctorProfileFragment)
            }

        }

        binding!!.patienttxtProfile.setOnClickListener {
            if (sharedPreferenceManager?.getString(DOCTOR_BOTTOMNAV)
                    .toString() == "ProfilePatient"
            ) {
                getBaseActivity().onBackPressed()
            } else {
                when (sharedPreferenceManager?.getString(DOCTOR_BOTTOMNAV)
                    .toString()) {
                    "HomePatient" -> {
                        navControllerPatient!!.popBackStack(R.id.patientHomeFragment, true)
                    }
                    "HistoryPatient" -> {
                        navControllerPatient!!.popBackStack(R.id.patientHistoryFragment, true)
                    }
                    "FitnessPatient" -> {
                        navControllerPatient!!.popBackStack(R.id.patientFitnessFragment, true)
                    }
                }
                sharedPreferenceManager?.putValue(
                    DOCTOR_BOTTOMNAV,
                    "ProfilePatient"
                )
                navControllerPatient!!.navigate(R.id.patientProfileFragment)
            }

        }

        binding!!.txtSetting.setOnClickListener {
            sharedPreferenceManager?.putValue(
                DOCTOR_BOTTOMNAV,
                ""
            )
            navControllerHome.navigate(R.id.doctorSettingFragment)
        }
        binding!!.txtChat.setOnClickListener {
            sharedPreferenceManager?.putValue(
                DOCTOR_BOTTOMNAV,
                ""
            )
            navControllerHome.navigate(R.id.chatlistDoctorFragment)
        }

        binding!!.patienttxtChat.setOnClickListener {
            sharedPreferenceManager?.putValue(
                DOCTOR_BOTTOMNAV,
                ""
            )
            navControllerPatient!!.navigate(R.id.chatlistDoctorFragment2)
        }




        binding!!.patienttxtAppointment.setOnClickListener {
            navControllerPatient!!.navigate(R.id.doctorListFragment2)
        }

        binding!!.patientConnectWatch.setOnClickListener {



            getUserForWatch()

            userObj?.watch_connectivity
            userObj?.watch_user_id

            Log.d("watch",userObj.toString())
            if (userObj?.watch_user_id.isNullOrEmpty())
            {

                Toast.makeText(requireContext(), "You can't connect watch at the moment, please contact administrator to resolve the issue", Toast.LENGTH_SHORT).show()
            }

            else if (userObj?.watch_user_id!=null && userObj?.watch_connectivity==0)
            {
                navControllerPatient!!.navigate(R.id.QRFragment)
            }
            else if (userObj?.watch_user_id!=null && userObj?.watch_connectivity==1 && userObj?.watch_calibration==1)
            {
                navControllerPatient!!.navigate(R.id.watchConnectedFragment)
            }
            else if (userObj?.watch_user_id!=null && userObj?.watch_connectivity==1 && userObj?.watch_calibration==0)
            {
                navControllerPatient!!.navigate(R.id.calibrateFragment)
            }

        }

        binding!!.subscriptiontxtHome.setOnClickListener {
            navControllerPatient!!.navigate(R.id.subscription2)
        }

        binding!!.docsubscription.setOnClickListener {
            navControllerHome!!.navigate(R.id.subscription)
        }


        binding!!.schtHome.setOnClickListener {
            navControllerHome!!.navigate(R.id.weeklySchedule2)
        }


        binding!!.patienttxtInvite.setOnClickListener {
            sharedPreferenceManager?.putValue(
                DOCTOR_BOTTOMNAV,
                ""
            )
            navControllerPatient!!.navigate(R.id.inviteFragment)
        }

        binding!!.patienttxtReset.setOnClickListener {
            sharedPreferenceManager?.putValue(
                DOCTOR_BOTTOMNAV,
                ""
            )
          // navControllerPatient!!.navigate(R.id.resetPassFragment2)
            navControllerHome.navigate(R.id.changePassFragment2)
        }


        /*binding!!.patienttxtInvite.setOnClickListener {
            navControllerHome.navigate(R.id.inviteFragment)
        }
*/

        binding!!.txtAlarm.setOnClickListener {
            sharedPreferenceManager?.putValue(
                DOCTOR_BOTTOMNAV,
                ""
            )
            navControllerHome.navigate(R.id.alarmDoctorFragment)
        }

        binding!!.patienttxtAlarm.setOnClickListener {
            sharedPreferenceManager?.putValue(
                DOCTOR_BOTTOMNAV,
                ""
            )
            navControllerPatient!!.navigate(R.id.alarmDoctorFragment2)
        }


        binding!!.edit.setOnClickListener {
            sharedPreferenceManager?.putValue(
                DOCTOR_BOTTOMNAV,
                ""
            )
            if (sharedPreferenceManager?.getString(SELECT_USER).toString() == "Doctor") {
                navControllerHome.navigate(R.id.doctorEditProfileFragment)
            } else {
                navControllerPatient!!.navigate(R.id.doctorEditFragment2)
            }

        }

        binding!!.txtReset.setOnClickListener {
            sharedPreferenceManager?.putValue(
                DOCTOR_BOTTOMNAV,
                ""
            )

            if (sharedPreferenceManager?.getString(SELECT_USER).toString() == "Doctor") {
                navControllerHome.navigate(R.id.changePassFragment)
            } else {
                navControllerHome.navigate(R.id.changePassFragment2)
            }
        }





        binding!!.patienttxtListDoc.setOnClickListener {
            sharedPreferenceManager?.putValue(
                DOCTOR_BOTTOMNAV,
                ""
            )
            navControllerPatient!!.navigate(R.id.listOfDoctorsFragment)
        }

        binding!!.patienttxtSetting.setOnClickListener {
            sharedPreferenceManager?.putValue(
                DOCTOR_BOTTOMNAV,
                ""
            )
            navControllerPatient!!.navigate(R.id.patientSettingFragment)
        }

        binding!!.txtLogout.setOnClickListener {
           // logout()
            closeApp1()
        }
        binding!!.patienttxtLogout.setOnClickListener {
            closeApp1()
            //logout()

        }

        binding?.txtDelete?.setOnClickListener {

            deletePopup()
        }

        binding?.txtDocDelete?.setOnClickListener {
            deletePopup()
        }

    /*    binding!!.patienttxtdata.setOnClickListener {
            navControllerPatient!!.navigate(R.id.patientDataFragment)

        }*/

    /*    binding!!.txtdata.setOnClickListener {
            navControllerHome!!.navigate(R.id.doctorDataFragment)

        }*/
    }

    private fun deleteAccount()
    {


        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.deleteAccount().enqueue(object: retrofit2.Callback<CommonResponse> {
                override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>
                )
                {
//                    activity?.runOnUiThread(java.lang.Runnable {
//                        binding.pbChangePass.visibility=View.GONE
//                        binding.btnSignUp.isEnabled=true
//                        binding.btnSignUp.text="RESET"
//
//                    })

                    response?.body()?.message?.let { Log.d("Response", it) }

                    try {

                        if (response.isSuccessful) {

                            Log.d("statusCode",response?.body()?.status.toString())


                            if (response.message() == "Unauthenticated." || response.message() == "Unauthorized" ||
                                response?.body()?.message == "Unauthenticated." || response?.body()?.message == "Unauthorized"
                            )
                            {
                                PreferenceUtils.remove(USER_OBJECT)
                                PreferenceUtils.remove(ACCESS_TOKEN)
                                //  startActivity(Intent(requireContext(),MainActivity::class.java))

                                if (sharedPreferenceManager?.getString(SELECT_USER).toString() == "Doctor") {
                                    PreferenceUtils.remove(USER_OBJECT)
                                    PreferenceUtils.remove(ACCESS_TOKEN)
                                    sharedPreferenceManager!!.clearDB()

                                    doctorData = null
                                    patientFitnessdata = null
                                    arrayFitnessList = null
                                    //navControllerHome?.navigate(R.id.preLoginFragment2)
                                    // MainActivity.mainActivity.finish()
                                    // HomeActivity.home.finish()
                                    startActivity(Intent(requireActivity(),MainActivity::class.java))
                                    HomeActivity.home.finish()
                                } else {
                                    PreferenceUtils.remove(USER_OBJECT)
                                    PreferenceUtils.remove(ACCESS_TOKEN)
                                    sharedPreferenceManager!!.clearDB()

                                    doctorData = null
                                    patientFitnessdata = null
                                    arrayFitnessList = null
                                    // navControllerPatient?.navigate(R.id.preLoginFragment3)
                                    // activity?.finish()
                                    startActivity(Intent(requireActivity(),MainActivity::class.java))
                                    HomeActivityPateint.home.finish()
                                }


                            }

                            if (response?.body()?.status == 1)
                            {



                                if (sharedPreferenceManager?.getString(SELECT_USER).toString() == "Doctor") {
                                    //MainActivity.navController.navigate(R.id.loginFragment)


                                    PreferenceUtils.remove(USER_OBJECT)
                                    PreferenceUtils.remove(ACCESS_TOKEN)
                                    sharedPreferenceManager!!.clearDB()

                                    doctorData = null
                                    patientFitnessdata = null
                                    arrayFitnessList = null
                                    //navControllerHome?.navigate(R.id.preLoginFragment2)
                                    // MainActivity.mainActivity.finish()
                                    // HomeActivity.home.finish()
                                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(requireActivity(),MainActivity::class.java))
                                    HomeActivity.home.finish()
                                    //MainActivity.navController.navigate(R.id.preLoginFragment)
                                } else {

                                    PreferenceUtils.remove(USER_OBJECT)
                                    PreferenceUtils.remove(ACCESS_TOKEN)
                                    sharedPreferenceManager!!.clearDB()

                                    doctorData = null
                                    patientFitnessdata = null
                                    arrayFitnessList = null
                                    // navControllerPatient?.navigate(R.id.preLoginFragment3)
                                    // activity?.finish()
                                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(requireActivity(),MainActivity::class.java))
                                    HomeActivityPateint.home.finish()
                                    //   MainActivity.navController.navigate(R.id.preLoginFragment)
                                }










                                /*  if (activity is HomeActivity)
                                  {
                                      HomeActivity.home.finish()
                                  }
                                  activity?.finish()
                                  MainActivity.navController.navigate(R.id.loginFragment)*/


                                // FirebaseInstanceId.getInstance().deleteInstanceId()

                            }
                        }
                        else {
                            activity?.runOnUiThread(java.lang.Runnable {
                                Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                            })
                        }
                    }
                    catch (e:Exception)
                    {
                        //Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                        activity?.runOnUiThread(java.lang.Runnable {
                            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                        })
                    }
                }

                override fun onFailure(call: Call<CommonResponse>, t: Throwable)
                {

                    activity?.runOnUiThread(java.lang.Runnable {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    })
                }
            })

        }

    }


    private fun logout()
    {
        PreferenceUtils.remove(USER_OBJECT)

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.logout().enqueue(object: retrofit2.Callback<CommonResponse> {
                override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>
                )
                {
//                    activity?.runOnUiThread(java.lang.Runnable {
//                        binding.pbChangePass.visibility=View.GONE
//                        binding.btnSignUp.isEnabled=true
//                        binding.btnSignUp.text="RESET"
//
//                    })

                    response?.body()?.message?.let { Log.d("Response", it) }

                    try {

                        if (response.isSuccessful) {



                            if (response.message() == "Unauthenticated." || response.message() == "Unauthorized" ||
                                response?.body()?.message == "Unauthenticated." || response?.body()?.message == "Unauthorized"
                                    )
                            {
                                PreferenceUtils.remove(USER_OBJECT)
                                PreferenceUtils.remove(ACCESS_TOKEN)
                              //  startActivity(Intent(requireContext(),MainActivity::class.java))

                                if (sharedPreferenceManager?.getString(SELECT_USER).toString() == "Doctor") {
                                    PreferenceUtils.remove(USER_OBJECT)
                                    PreferenceUtils.remove(ACCESS_TOKEN)
                                    sharedPreferenceManager!!.clearDB()

                                    doctorData = null
                                    patientFitnessdata = null
                                    arrayFitnessList = null
                                    //navControllerHome?.navigate(R.id.preLoginFragment2)
                                    // MainActivity.mainActivity.finish()
                                    // HomeActivity.home.finish()
                                    startActivity(Intent(requireActivity(),MainActivity::class.java))
                                    HomeActivity.home.finish()
                                } else {
                                    PreferenceUtils.remove(USER_OBJECT)
                                    PreferenceUtils.remove(ACCESS_TOKEN)
                                    sharedPreferenceManager!!.clearDB()

                                    doctorData = null
                                    patientFitnessdata = null
                                    arrayFitnessList = null
                                    // navControllerPatient?.navigate(R.id.preLoginFragment3)
                                    // activity?.finish()
                                    startActivity(Intent(requireActivity(),MainActivity::class.java))
                                    HomeActivityPateint.home.finish()
                                }


                            }

                            if (response?.body()?.status == 1)
                            {



                                if (sharedPreferenceManager?.getString(SELECT_USER).toString() == "Doctor") {
                                    //MainActivity.navController.navigate(R.id.loginFragment)


                                    PreferenceUtils.remove(USER_OBJECT)
                                    PreferenceUtils.remove(ACCESS_TOKEN)
                                    sharedPreferenceManager!!.clearDB()

                                    doctorData = null
                                    patientFitnessdata = null
                                    arrayFitnessList = null
                                    //navControllerHome?.navigate(R.id.preLoginFragment2)
                                   // MainActivity.mainActivity.finish()
                                   // HomeActivity.home.finish()
                                    startActivity(Intent(requireActivity(),MainActivity::class.java))
                                    HomeActivity.home.finish()
                                    //MainActivity.navController.navigate(R.id.preLoginFragment)
                                } else {

                                    PreferenceUtils.remove(USER_OBJECT)
                                    PreferenceUtils.remove(ACCESS_TOKEN)
                                    sharedPreferenceManager!!.clearDB()

                                    doctorData = null
                                    patientFitnessdata = null
                                    arrayFitnessList = null
                                   // navControllerPatient?.navigate(R.id.preLoginFragment3)
                                   // activity?.finish()
                                    startActivity(Intent(requireActivity(),MainActivity::class.java))
                                     HomeActivityPateint.home.finish()
                                 //   MainActivity.navController.navigate(R.id.preLoginFragment)
                                }










                              /*  if (activity is HomeActivity)
                                {
                                    HomeActivity.home.finish()
                                }
                                activity?.finish()
                                MainActivity.navController.navigate(R.id.loginFragment)*/


                               // FirebaseInstanceId.getInstance().deleteInstanceId()

                            }
                        }
                        else {
                            activity?.runOnUiThread(java.lang.Runnable {
                                Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                            })
                        }
                    }
                    catch (e:Exception)
                    {
                        //Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                        activity?.runOnUiThread(java.lang.Runnable {
                            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                        })
                    }
                }

                override fun onFailure(call: Call<CommonResponse>, t: Throwable)
                {

                     activity?.runOnUiThread(java.lang.Runnable {
                         Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                     })
                }
            })

        }


    }

    companion object {
        var leftSideMenuFragment = LeftSideMenuFragment()
        fun newInstance(): LeftSideMenuFragment {


            val args = Bundle()
            val fragment = LeftSideMenuFragment()
            fragment.arguments = args
            return fragment
        }
    }


    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onResume() {
        super.onResume()
        getUser()

      // requireContext().hideKeyboard(this@LeftSideMenuFragment)
    }

    fun closeApp1() {
        val dialog1 = Dialog(requireContext())
        dialog1.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog1.setContentView(R.layout.dialog_logout)
        dialog1.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog1.show()
        val txtYes = dialog1.findViewById<AppCompatTextView>(R.id.txtYes)
        val txtNo = dialog1.findViewById<AppCompatTextView>(R.id.txtNo)
        val tvDes = dialog1.findViewById<AppCompatTextView>(R.id.tv_des)
        tvDes.setText("Are you sure you want to logout?")

        txtNo.setOnClickListener { dialog1.dismiss() }
        txtYes.setOnClickListener {
            dialog1.dismiss()
            logout()
            //getBaseActivity().openActivity(MainActivity::class.java)
        }
    }

    fun deletePopup() {
        val dialog1 = Dialog(requireContext())
        dialog1.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog1.setContentView(R.layout.dialog_logout)
        dialog1.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog1.show()
        val txtYes = dialog1.findViewById<AppCompatTextView>(R.id.txtYes)
        val txtNo = dialog1.findViewById<AppCompatTextView>(R.id.txtNo)
        val tvDes = dialog1.findViewById<AppCompatTextView>(R.id.tv_des)
        tvDes.setText("Are you sure you want to delete?")

        txtNo.setOnClickListener { dialog1.dismiss() }
        txtYes.setOnClickListener {
            dialog1.dismiss()
            deleteAccount()
            //getBaseActivity().openActivity(MainActivity::class.java)
        }
    }

}