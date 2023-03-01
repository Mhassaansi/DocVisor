package com.fictivestudios.docsvisor.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.core.widget.TextViewCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.constants.DOCTOR_BOTTOMNAV
import com.fictivestudios.docsvisor.enums.FCMEnums
import com.fictivestudios.docsvisor.helper.KeyboardHelper
import com.fictivestudios.docsvisor.helper.SharedPreferenceManager
import com.google.android.material.navigation.NavigationView
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import kotlinx.android.synthetic.main.bottom_doctor_nav.*


class HomeActivity : BaseActivity() {

    var navigationView: NavigationView? = null
    var contMain: FrameLayout? = null
    var contParentActivityLayout: LinearLayoutCompat? = null
    var doctorHome: LinearLayoutCompat? = null
    var doctorProfile: LinearLayoutCompat? = null
    private var sharedPreferenceManager: SharedPreferenceManager? = null
    private var containerBottomBar: LinearLayoutCompat? = null


    companion object {
        var home = HomeActivity()
        lateinit var navControllerHome: NavController
    }

    protected override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)


    }

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        sharedPreferenceManager = SharedPreferenceManager.getInstance(this)
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this))
        home = this
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.contMain) as NavHostFragment
        navControllerHome = navHostFragment.navController




        if (intent?.extras!=null)
        {
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
                    ,"username" to name)
                navControllerHome.navigate(R.id.messengerFragment,bundle)
            }
            else if (type == FCMEnums.AUDIO.value)
            {
                var token=intent?.extras?.getString("token").toString()
                var channel=intent?.extras?.getString("channel").toString()

                val bundle = bundleOf(
                    "token" to token
                    ,"channel" to channel
                    ,"name" to name
                    ,"type" to type)

                navControllerHome.navigate(R.id.audioFragment,bundle)
            }
            else if (type == FCMEnums.VIDEO.value)
            {
                var token=intent?.extras?.getString("token").toString()
                var channel=intent?.extras?.getString("channel").toString()

                val bundle = bundleOf(
                    "token" to token
                    ,"channel" to channel
                    ,"name" to name
                    ,"type" to type)

                navControllerHome.navigate(R.id.videoFragment,bundle)
            }
            else if (type == FCMEnums.APPOINTMENT.value)
            {
                navControllerHome.navigate(R.id.appointmentTaB)
            }
            else if (type == FCMEnums.ALARM.value)
            {
                navControllerHome.navigate(R.id.alarmDoctorFragment)
            }

            Log.d("extraString",intent?.extras?.getString("postId").toString())
        }
    }

    override val view_Id: Int
        get() = R.layout.activity_home

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
        doctorHome = findViewById(R.id.doctorHome)

        containerBottomBar = findViewById(R.id.contBottomBar);
        setListeners()


    }


    private fun setListeners() {

        doctorProfile!!.setOnClickListener {
            if (sharedPreferenceManager?.getString(DOCTOR_BOTTOMNAV).toString() == "Profile") {
            } else {

                profName.setTextColor(Color.parseColor("#6f64fe"))
                homeName.setTextColor(Color.parseColor("#6d7379"))
                profImage.setColorFilter(getResources().getColor(R.color.colorPrimary));
                homeImage.setColorFilter(getResources().getColor(R.color.item_text_new));
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

        doctorHome!!.setOnClickListener {
            if (sharedPreferenceManager?.getString(DOCTOR_BOTTOMNAV).toString() == "Home") {


            } else {

                profName.setTextColor(Color.parseColor("#6d7379"))
                homeName.setTextColor(Color.parseColor("#6f64fe"))
                profImage.setColorFilter(getResources().getColor(R.color.item_text_new));
                homeImage.setColorFilter(getResources().getColor(R.color.colorPrimary));
                if (sharedPreferenceManager?.getString(DOCTOR_BOTTOMNAV).toString() == "Profile") {
                    navControllerHome.popBackStack(R.id.doctorProfileFragment, true)
                }
                sharedPreferenceManager?.putValue(
                    DOCTOR_BOTTOMNAV,
                    "Home"
                )
                navControllerHome.navigate(R.id.doctorPatientFragment)

            }
        }

    }

    override fun onBackPressed() {
        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawer(GravityCompat.START)
        } else {
//            navControllerHome.navigateUp()
            if (navControllerHome.currentDestination!!.id == R.id.doctorPatientFragment) {
                closeApp()
            }else if(navControllerHome.currentDestination!!.id == R.id.doctorProfileFragment) {
                closeApp()
            }
            else if(navControllerHome.currentDestination!!.id == R.id.chatlistDoctorFragment) {
                navControllerHome.popBackStack()
                navControllerHome.navigate(R.id.doctorPatientFragment)
            }
            else {
                navControllerHome.popBackStack()
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


        }
    }





    /*   fun getBlurImage(): ImageView? {
           return imageBlur
       }*/

    fun setBlurBackground() {

        // For Future use

////        if (mBackgroundFilename == null) {
//
//        this.mDownScaled = Utils.drawViewToBitmap(this.getMainContentFrame(), Color.parseColor("#fff5f5f5"));
//
//        mBackgroundFilename = getBlurredBackgroundFilename();
//        if (!TextUtils.isEmpty(mBackgroundFilename)) {
//            //context.getMainContentFrame().setVisibility(View.VISIBLE);
//            background = Utils.loadBitmapFromFile(mBackgroundFilename);
////                if (background != null) {
//            getBlurImage().setVisibility(View.VISIBLE);
//            getBlurImage().setImageBitmap(background);
//            getBlurImage().animate().alpha(1);
////                }
//        }
////        } else {
////            getBlurImage().setVisibility(View.VISIBLE);
////            getBlurImage().setImageBitmap(background);
////            getBlurImage().animate().alpha(1);
////        }
    }

    /*fun getBlurredBackgroundFilename(): String? {
      *//*  val localBitmap: Bitmap = Blur.fastblur(this, mDownScaled, 20)
        val str: String = Utils.saveBitmapToFile(this, localBitmap)
        mDownScaled!!.recycle()
        localBitmap.recycle()
        return str*//*
    }
*/
    fun removeBlurImage() {
        //  getBlurImage()?.setVisibility(View.GONE)
    }

    fun test() {
        KeyboardHelper.hideSoftKeyboard(
            this, window.decorView
        )
    }


    fun doctorPatientFragment() {
        profName.setTextColor(Color.parseColor("#6d7379"))
        homeName.setTextColor(Color.parseColor("#6f64fe"))
        profImage.setColorFilter(getResources().getColor(R.color.item_text_new))
        homeImage.setColorFilter(getResources().getColor(R.color.colorPrimary))
    }

    fun doctorProfile() {
        profName.setTextColor(Color.parseColor("#6f64fe"))
        homeName.setTextColor(Color.parseColor("#6d7379"))
        profImage.setColorFilter(getResources().getColor(R.color.colorPrimary));
        homeImage.setColorFilter(getResources().getColor(R.color.item_text_new));
    }

    fun showBottomBar() {
        if (containerBottomBar != null) containerBottomBar!!.visibility = View.VISIBLE
    }

    public fun hideBottomBar() {
        if (containerBottomBar != null) containerBottomBar!!.visibility = View.GONE
    }

}