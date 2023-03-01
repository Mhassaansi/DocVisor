package com.fictivestudios.docsvisor.activities

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.constants.SELECT_USER
import com.fictivestudios.docsvisor.enums.FCMEnums
import com.fictivestudios.docsvisor.helper.PreferenceUtils
import com.fictivestudios.docsvisor.helper.SharedPreferenceManager
import com.fictivestudios.docsvisor.helper.USER_OBJECT
import com.fictivestudios.docsvisor.model.PushAgoraData
import com.fictivestudios.docsvisor.model.PushNotification
import com.fictivestudios.docsvisor.model.User
import com.google.gson.Gson
import com.mikhaellopez.rxanimation.RxAnimation
import com.mikhaellopez.rxanimation.fadeIn
import com.mikhaellopez.rxanimation.resize
import kotlinx.android.synthetic.main.activity_splash.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class Splash : AppCompatActivity() {

    private val ANIMATIONS_TIME_OUT: Long = 3000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        printHashKey(baseContext)

        PreferenceUtils.init(applicationContext)

        //        contParentLayout.setVisibility(View.INVISIBLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        //   val txtVersionNumber: TextView = findViewById(R.id.txtVersionNumber)

        try {
            val manager = packageManager
            val info = manager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            //txtVersionNumber.text = "Build Version: " + info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            //txtVersionNumber.text = ""
            e.printStackTrace()
        }




        var gson =  Gson()
        var json:String= PreferenceUtils.getString(USER_OBJECT)
        var userObj: User?=gson.fromJson(json, User::class.java)


        if (intent?.extras?.getString("title") != null) {

            Log.e("extras", "" + intent?.extras?.getString("title") )


            if (intent?.extras?.getString("type").toString() == FCMEnums.AUDIO.value
                ||intent?.extras?.getString("type").toString() == FCMEnums.VIDEO.value)
            {
                val data = PushAgoraData(
                      intent?.extras?.getString("channel").toString(),
                      intent?.extras?.getString("type").toString(),
                      intent?.extras?.getString("user").toString(),
                      intent?.extras?.getString("token").toString(),
                      intent?.extras?.getString("user_name").toString()
                )

                var intent : Intent? = null

                if (PreferenceUtils
                        ?.getString(SELECT_USER) == "Doctor"
                )
                { intent = Intent(this,HomeActivity::class.java)}

                else
                { intent = Intent(this,HomeActivityPateint::class.java)}



                // val bundle = bundleOf("data" to data)

                intent.putExtra("token",data.token)
                intent.putExtra("channel",data.channel)
                intent.putExtra("type",data.type)
                intent.putExtra("user_name",data.user_name)
                intent.putExtra("user",data.user)



                startActivity(intent)

            }
            else
            {
                val data = PushNotification(
                    intent?.extras?.getString("body").toString(),
                    intent?.extras?.getString("postId").toString(),
                    intent?.extras?.getString("title").toString(),
                    intent?.extras?.getString("type").toString(),

                    )

                var intent : Intent? = null


                if (PreferenceUtils
                        ?.getString(SELECT_USER) == "Doctor"
                )
                { intent = Intent(this,HomeActivity::class.java)}

                else
                { intent = Intent(this,HomeActivityPateint::class.java)}

                // val bundle = bundleOf("data" to data)

                intent.putExtra("postId",data.postId)
                intent.putExtra("title",data.title)
                intent.putExtra("type",data.type)

                startActivity(intent)
            }



           /* if (SharedPreferenceManager.getInstance(applicationContext)
                    ?.getString(SELECT_USER) == "Doctor"
            )
            {
                intent = Intent(this,HomeActivity::class.java)

                intent.putExtra("postId",data.postId)
                intent.putExtra("title",data.title)
                intent.putExtra("type",data.type)
                startActivity(intent)
            }
            else
            {

            }*/

        }

       else if (userObj != null) {
            if (SharedPreferenceManager.getInstance(applicationContext)
                    ?.getString(SELECT_USER) == "Doctor"
            ) {
            changeActivity(HomeActivity::class.java)

            } else {
                changeActivity(HomeActivityPateint::class.java)


            }


        } else {
            changeActivity(MainActivity::class.java)

        }

    }

    private fun changeActivity(activityClass: Class<*>) {
        RxAnimation.together(
            imgLogo.fadeIn(ANIMATIONS_TIME_OUT),
            //  imgLogo.rotation(360f, ANIMATIONS_TIME_OUT )
            imgLogo.resize(300, 300, ANIMATIONS_TIME_OUT)
        ).subscribe()



        Handler().postDelayed(/*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            {
                val i: Intent = Intent(this@Splash, activityClass)
                // This method will be executed once the timer is over
                // Start your app main activity

                startActivity(i)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                // close this activity
                finish()
            }, 1000 + ANIMATIONS_TIME_OUT.toLong()
        )
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if (hasFocus) {


        }
    }


    override fun onDestroy() {

        super.onDestroy()

    }



  /*  private fun navigateToDestination(data: PushNotification)
    {
        var pendingIntent: PendingIntent? = null
        if (PreferenceUtils
                ?.getString(SELECT_USER) == "Doctor"
        )

        {
            pendingIntent = when(data.type){
                FCMEnums.CHAT .value -> {
                    val bundle = bundleOf("receiverUserId" to data.postId
                        ,"username" to data.title)
                    getPatientPendingIntent(destinationId = R.id.messengerFragment2,bundle)
                }
                FCMEnums.APPOINTMENT .value -> getPatientPendingIntent(destinationId = R.id.notificationDoctorFragment2)
                FCMEnums.VIDEO .value -> getPatientPendingIntent(destinationId = R.id.videoFragment2)
                FCMEnums.AUDIO .value -> getPatientPendingIntent(destinationId = R.id.audioFragment2)
                FCMEnums.ALARM .value -> getPatientPendingIntent(destinationId = R.id.alarmDoctorFragment2)
                else -> null
            }

        }
        else
        {
            pendingIntent = when(data.type){
                FCMEnums.CHAT .value -> {
                    val bundle = bundleOf("receiverUserId" to data.postId
                        ,"username" to data.title)
                    getDoctorPendingIntent(destinationId = R.id.messengerFragment,bundle)
                }
                FCMEnums.APPOINTMENT .value -> getDoctorPendingIntent(destinationId = R.id.notificationDoctorFragment)
                FCMEnums.VIDEO .value -> getDoctorPendingIntent(destinationId = R.id.videoFragment)
                FCMEnums.AUDIO .value -> getDoctorPendingIntent(destinationId = R.id.audioFragment)
                FCMEnums.ALARM .value -> getDoctorPendingIntent(destinationId = R.id.alarmDoctorFragment)
                else -> null
            }
        }
    }*/
/*
    private fun getPatientPendingIntent(destinationId: Int, bundle: Bundle? = null): PendingIntent =
        NavDeepLinkBuilder(applicationContext)
            .setComponentName(HomeActivityPateint::class.java)
            .setGraph(R.navigation.pateint_module_graph)
            .setDestination(destinationId)
            .setArguments(bundle)
            .createPendingIntent()

    *//**
     * Pending Intent Handle
     * *//*
    private fun getDoctorPendingIntent(destinationId: Int, bundle: Bundle? = null): PendingIntent =
        NavDeepLinkBuilder(applicationContext)
            .setComponentName(HomeActivity::class.java)
            .setGraph(R.navigation.doctor_module_graph)
            .setDestination(destinationId)
            .setArguments(bundle)
            .createPendingIntent()*/

    fun printHashKey(context: Context) { // Add code to print out the key hash
        try {
            val info = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }

    companion object {
        private val TAG = "SPLASH SCREEN"
    }
}