package com.fictivestudios.docsvisor.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.callbacks.GetMessagesData
import com.fictivestudios.docsvisor.callbacks.ReceivedMessages
import com.fictivestudios.docsvisor.callbacks.SendMessage
import com.fictivestudios.docsvisor.constants.DOCTOR_BOTTOMNAV
import com.fictivestudios.docsvisor.enums.FCMEnums
import com.fictivestudios.docsvisor.helper.KeyboardHelper
import com.fictivestudios.docsvisor.helper.SharedPreferenceManager
import com.gigo.clean.networksetup.socket.SocketApp
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import io.socket.client.Ack
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.bottom_pateint_nav.*
import org.json.JSONObject


class HomeActivityPateint : BaseActivity(){

    var navigationView: NavigationView? = null
    var contMain: FrameLayout? = null
    var contParentActivityLayout: LinearLayoutCompat? = null
    var doctorHome: LinearLayoutCompat? = null
    var doctorProfile: LinearLayoutCompat? = null
    var doctorHistory: LinearLayoutCompat? = null
    var doctorFitness: LinearLayoutCompat? = null
    private var sharedPreferenceManager: SharedPreferenceManager? = null
    private var containerBottomBar: LinearLayoutCompat? = null


    var receivedMessages:ReceivedMessages?=null

    companion object {

        var home = HomeActivityPateint()
        var navControllerPatient: NavController? = null

    }

    protected override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.hasExtra("data") == true)
        {
            Log.d("extra",intent.getBundleExtra("data").toString())
        }
    }

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        sharedPreferenceManager = SharedPreferenceManager.getInstance(this)
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this))
        home = this
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.contMain) as NavHostFragment
         navControllerPatient = navHostFragment.navController


        if (intent?.extras!=null)
        {
            if (intent?.extras?.getString("type").toString() == FCMEnums.AUDIO.value
                ||intent?.extras?.getString("type").toString() == FCMEnums.VIDEO.value)
            {
                 if (intent?.extras?.getString("type").toString() == FCMEnums.AUDIO.value)
            {
                var token=intent?.extras?.getString("token").toString()
                var channel=intent?.extras?.getString("channel").toString()
                var username=intent?.extras?.getString("user_name").toString()
                var type=intent?.extras?.getString("type").toString()
                var otherId=intent?.extras?.getString("other_id").toString()

                val bundle = bundleOf(
                    "token" to token
                    ,"channel" to channel
                    ,"name" to username
                    ,"type" to type
                ,"other_id" to otherId
                )

                HomeActivityPateint.navControllerPatient?.navigate(R.id.callerScreenFragment,bundle)
              //  navControllerPatient?.popBackStack(R.id.callerScreenFragment,true)
            }
            else if (intent?.extras?.getString("type").toString() == FCMEnums.VIDEO.value)
            {
                var token=intent?.extras?.getString("token").toString()
                var channel=intent?.extras?.getString("channel").toString()
                var username=intent?.extras?.getString("user_name").toString()
                var type=intent?.extras?.getString("type").toString()
                var otherId=intent?.extras?.getString("other_id").toString()

                val bundle = bundleOf(
                    "token" to token
                    ,"channel" to channel
                    ,"name" to username
                    ,"type" to type
                    ,"other_id" to otherId
                )

                HomeActivityPateint.navControllerPatient?.navigate(R.id.callerScreenFragment,bundle)
               // navControllerPatient?.popBackStack(R.id.callerScreenFragment,true)
            }
            }
            else{
                var type=intent?.extras?.getString("type").toString()
                var receiverUserId=intent?.extras?.getString("postId").toString()
                var name = intent?.extras?.getString("title").toString()

/*            val bundle = bundleOf(
                "receiverUserId" to receiverUserId
                ,"username" to name)
            navControllerHome.navigate(R.id.messengerFragment,bundle)*/


                if (type == FCMEnums.CHAT.value)
                {


                    val bundle = bundleOf(
                        "receiverUserId" to receiverUserId
                        ,"title" to name)
                    HomeActivityPateint.navControllerPatient?.navigate(R.id.messengerFragment2,bundle)
                }

                else if (type == FCMEnums.APPOINTMENT.value)
                {
                    HomeActivityPateint.navControllerPatient?.navigate(R.id.doctorListFragment2)
                }
                else if (type == FCMEnums.ALARM.value)
                {
                    HomeActivityPateint.navControllerPatient?.navigate(R.id.alarmDoctorFragment2)
                }

                Log.d("extraString",intent?.extras?.getString("postId").toString())
            }




        }
    }

    override val view_Id: Int
        get() = R.layout.activity_home_pateint

    override val titlebarLayoutId: Int
        get() = R.id.titlebar

    override val drawerLayoutId: Int
        get() = R.id.drawer_layout

    override val dockableFragmentId: Int
        get() = R.id.contMain

    override val drawerFragmentId: Int
        get() = R.id.contDrawer

    protected override fun onPostCreate(@Nullable savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        //  RunTimePermissions.verifyStoragePermissions(this)
        navigationView = findViewById(R.id.nav_view)
        navigationView!!.background.setColorFilter(-0x80000000, PorterDuff.Mode.MULTIPLY)
        contMain = findViewById(R.id.contMain)
        contParentActivityLayout = findViewById(R.id.contParentActivityLayout)
        doctorProfile = findViewById(R.id.doctorProfile)
        doctorHistory = findViewById(R.id.doctorHistory)
        doctorFitness = findViewById(R.id.doctorFitness)
        doctorHome = findViewById(R.id.doctorHome)
        containerBottomBar = findViewById(R.id.contBottomBar);
        setListeners()
    }




















    override fun onBackPressed() {
        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawer(GravityCompat.START)
        } else {
//            navControllerHome.navigateUp()
            if (navControllerPatient!!.currentDestination!!.id == R.id.patientHomeFragment) {
                closeApp()
            } else if (navControllerPatient!!.currentDestination!!.id == R.id.patientFitnessFragment) {
                closeApp()
            } else if (navControllerPatient!!.currentDestination!!.id == R.id.patientHistoryFragment) {
                closeApp()
            } else if (navControllerPatient!!.currentDestination!!.id == R.id.patientProfileFragment) {
                closeApp()
            }
            else if (navControllerPatient!!.currentDestination!!.id == R.id.audioFragment2)
            {
                navControllerPatient!!.popBackStack()
                navControllerPatient!!.popBackStack()
            }
            else if (navControllerPatient!!.currentDestination!!.id == R.id.videoFragment2)
            {
                navControllerPatient!!.popBackStack()
                navControllerPatient!!.popBackStack()
            }
            else {
                navControllerPatient!!.popBackStack()
            }


            /*  if (supportFragmentManager.backStackEntryCount > 1) {
                  super.onBackPressed()
                  val fragments: List<Fragment> =
                      supportFragmentManager.fragments
                  val fragment = fragments[fragments.size - 1] as BaseFragment
                  fragment.setTitlebar(titleBar)
              } else {
                  //  moveTaskToBack(true)            }
                  closeApp()

              }*/


            /*  if (navControllerHome.currentDestination!!.id == R.id.dashboardFragment) {
                  closeApp()
                 }
              else {
                  //  moveTaskToBack(true)            }


                  navControllerHome.popBackStack()

              }
  */


/*

            when (navControllerHome.currentDestination?.id) {
                //   R.id.dashboardFragment, R.id.changePassFragment, R.id.notificationFragment, R.id.followersFragment, R.id.inAppPurchaseFragment -> {
//                R.id.dashboardFragment -> {
////                if (onBackPressedDispatcher.hasEnabledCallbacks())
////                    onBackPressedDispatcher.onBackPressed()
//                    // else
//                    ///  navController.navigateUp()
//                    closeApp()
//
//                }
//                else -> navControllerHome.popBackStack()
            }
*/


        }
    }


    private fun closeApp() {
        val dialog1 = Dialog(this)
        dialog1.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog1.setContentView(R.layout.dialog_exit)
        dialog1.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog1.show()
        val txtYes = dialog1.findViewById<AppCompatTextView>(R.id.txtYes)
        val txtNo = dialog1.findViewById<AppCompatTextView>(R.id.txtNo)

        txtNo.setOnClickListener { dialog1.dismiss() }
        txtYes.setOnClickListener {
            dialog1.dismiss()
            finish()
            /* val queryMap = HashMap<String, Any>()

             queryMap["user_id"] = sharedPreferenceManager!!.getCurrentUser()!!.userId
             WebServices(
                 this, sharedPreferenceManager!!.getString(KEY_TOKEN),
                 BaseURLTypes.BASE_URL,
                 true
             ).getAPIAnyObject(
                 WebServiceConstants.PATH_LOGOUT,
                 queryMap,
                 object : WebServices.IRequestWebResponseAnyObjectCallBack {
                     override fun requestDataResponse(webResponse: WebResponse<Any>) {
                         dialog1.dismiss()
                         SharedPreferenceManager.clearDB()
                         clearAllActivitiesExceptThis(MainActivity::class.java)
                         openActivity(MainActivity::class.java)
                         finish()
                     }

                     override fun onError(`object`: Any?) {
                         dialog1.dismiss()
                     }
                 })*/
        }
    }


    fun test() {
        KeyboardHelper.hideSoftKeyboard(
            this, window.decorView
        )
    }


    fun patientHomeFragment() {
        profName.setTextColor(Color.parseColor("#6d7379"))
        historyName.setTextColor(Color.parseColor("#6d7379"))
        fitnessName.setTextColor(Color.parseColor("#6d7379"))
        homeName.setTextColor(Color.parseColor("#6f64fe"))
        profImage.setColorFilter(getResources().getColor(R.color.item_text_new))
        historyImage.setColorFilter(getResources().getColor(R.color.item_text_new))
        fitnessImage.setColorFilter(getResources().getColor(R.color.item_text_new))
        homeImage.setColorFilter(getResources().getColor(R.color.colorPrimary))
    }

    fun patientProfile() {
        profName.setTextColor(Color.parseColor("#6f64fe"))
        homeName.setTextColor(Color.parseColor("#6d7379"))
        profImage.setColorFilter(getResources().getColor(R.color.colorPrimary));
        homeImage.setColorFilter(getResources().getColor(R.color.item_text_new));
        historyName.setTextColor(Color.parseColor("#6d7379"))
        fitnessName.setTextColor(Color.parseColor("#6d7379"))
        historyImage.setColorFilter(getResources().getColor(R.color.item_text_new))
        fitnessImage.setColorFilter(getResources().getColor(R.color.item_text_new))
    }

    fun patientHistory() {
        historyName.setTextColor(Color.parseColor("#6f64fe"))
        homeName.setTextColor(Color.parseColor("#6d7379"))
        historyImage.setColorFilter(getResources().getColor(R.color.colorPrimary));
        homeImage.setColorFilter(getResources().getColor(R.color.item_text_new));
        profName.setTextColor(Color.parseColor("#6d7379"))
        fitnessName.setTextColor(Color.parseColor("#6d7379"))
        profImage.setColorFilter(getResources().getColor(R.color.item_text_new))
        fitnessImage.setColorFilter(getResources().getColor(R.color.item_text_new))
    }

    fun patientFitness() {
        fitnessName.setTextColor(Color.parseColor("#6f64fe"))
        homeName.setTextColor(Color.parseColor("#6d7379"))
        fitnessImage.setColorFilter(getResources().getColor(R.color.colorPrimary));
        homeImage.setColorFilter(getResources().getColor(R.color.item_text_new));
        profName.setTextColor(Color.parseColor("#6d7379"))
        historyName.setTextColor(Color.parseColor("#6d7379"))
        profImage.setColorFilter(getResources().getColor(R.color.item_text_new))
        historyImage.setColorFilter(getResources().getColor(R.color.item_text_new))
    }

    fun showBottomBar() {
        if (containerBottomBar != null) containerBottomBar!!.visibility = View.VISIBLE
    }

    public fun hideBottomBar() {
        if (containerBottomBar != null) containerBottomBar!!.visibility = View.GONE
    }


    private fun setListeners() {

        doctorProfile!!.setOnClickListener {
            if (sharedPreferenceManager?.getString(DOCTOR_BOTTOMNAV)
                    .toString() == "ProfilePatient"
            ) {
            } else {

                profName.setTextColor(Color.parseColor("#6f64fe"))
                homeName.setTextColor(Color.parseColor("#6d7379"))
                historyName.setTextColor(Color.parseColor("#6d7379"))
                fitnessName.setTextColor(Color.parseColor("#6d7379"))
                profImage.setColorFilter(getResources().getColor(R.color.colorPrimary));
                homeImage.setColorFilter(getResources().getColor(R.color.item_text_new))
                fitnessImage.setColorFilter(getResources().getColor(R.color.item_text_new))
                historyImage.setColorFilter(getResources().getColor(R.color.item_text_new))
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
                    "FitnessPatient"
                )
                navControllerPatient!!.navigate(R.id.patientProfileFragment)

            }
        }

        doctorHome!!.setOnClickListener {
            if (sharedPreferenceManager?.getString(DOCTOR_BOTTOMNAV).toString() == "HomePatient") {
            } else {

                profName.setTextColor(Color.parseColor("#6d7379"))
                homeName.setTextColor(Color.parseColor("#6f64fe"))
                historyName.setTextColor(Color.parseColor("#6d7379"))
                fitnessName.setTextColor(Color.parseColor("#6d7379"))
                profImage.setColorFilter(getResources().getColor(R.color.item_text_new));
                homeImage.setColorFilter(getResources().getColor(R.color.colorPrimary))
                fitnessImage.setColorFilter(getResources().getColor(R.color.item_text_new))
                historyImage.setColorFilter(getResources().getColor(R.color.item_text_new))
                when (sharedPreferenceManager?.getString(DOCTOR_BOTTOMNAV)
                    .toString()) {
                    "HistoryPatient" -> {
                        navControllerPatient!!.popBackStack(R.id.patientHistoryFragment, true)
                    }
                    "ProfilePatient" -> {
                        navControllerPatient!!.popBackStack(R.id.patientProfileFragment,true)
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

        doctorHistory!!.setOnClickListener {
            if (sharedPreferenceManager?.getString(DOCTOR_BOTTOMNAV)
                    .toString() == "HistoryPatient"
            ) {
            } else {

                profName.setTextColor(Color.parseColor("#6d7379"))
                homeName.setTextColor(Color.parseColor("#6d7379"))
                historyName.setTextColor(Color.parseColor("#6f64fe"))
                fitnessName.setTextColor(Color.parseColor("#6d7379"))
                profImage.setColorFilter(getResources().getColor(R.color.item_text_new));
                homeImage.setColorFilter(getResources().getColor(R.color.item_text_new))
                fitnessImage.setColorFilter(getResources().getColor(R.color.item_text_new))
                historyImage.setColorFilter(getResources().getColor(R.color.colorPrimary))
                when (sharedPreferenceManager?.getString(DOCTOR_BOTTOMNAV)
                    .toString()) {
                    "HomePatient" -> {
                        navControllerPatient!!.popBackStack(R.id.patientHomeFragment, true)

                    }
                    "ProfilePatient" -> {
                        navControllerPatient!!.popBackStack(R.id.patientProfileFragment,true)
                    }
                    "FitnessPatient" -> {
                        navControllerPatient!!.popBackStack(R.id.patientFitnessFragment, true)
                    }
                }
                sharedPreferenceManager?.putValue(
                    DOCTOR_BOTTOMNAV,
                    "HistoryPatient"
                )
                navControllerPatient!!.navigate(R.id.patientHistoryFragment)

            }
        }

        doctorFitness!!.setOnClickListener {
            if (sharedPreferenceManager?.getString(DOCTOR_BOTTOMNAV)
                    .toString() == "FitnessPatient"
            ) {
            } else {

                profName.setTextColor(Color.parseColor("#6d7379"))
                homeName.setTextColor(Color.parseColor("#6d7379"))
                historyName.setTextColor(Color.parseColor("#6d7379"))
                fitnessName.setTextColor(Color.parseColor("#6d7379"))
                profImage.setColorFilter(getResources().getColor(R.color.item_text_new));
                homeImage.setColorFilter(getResources().getColor(R.color.item_text_new))
                fitnessImage.setColorFilter(getResources().getColor(R.color.colorPrimary))
                historyImage.setColorFilter(getResources().getColor(R.color.item_text_new))
                when (sharedPreferenceManager?.getString(DOCTOR_BOTTOMNAV)
                    .toString()) {
                    "HomePatient" -> {
                        navControllerPatient!!.popBackStack(R.id.patientHomeFragment, true)

                    }
                    "ProfilePatient" -> {
                        navControllerPatient!!.popBackStack(R.id.patientProfileFragment,true)
                    }
                    "HistoryPatient" -> {
                        navControllerPatient!!.popBackStack(R.id.patientHistoryFragment, true)
                    }
                }
                sharedPreferenceManager?.putValue(
                    DOCTOR_BOTTOMNAV,
                    "FitnessPatient"
                )
                navControllerPatient!!.navigate(R.id.patientFitnessFragment)

            }
        }

    }







}