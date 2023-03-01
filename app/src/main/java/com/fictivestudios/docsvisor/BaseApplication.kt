package com.fictivestudios.docsvisor

import android.app.Activity
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Pair
import androidx.multidex.MultiDexApplication
import com.fictivestudios.docsvisor.activities.MainActivity
import com.fictivestudios.docsvisor.libraries.CustomImageDownaloder
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import io.reactivex.subjects.PublishSubject


object BaseApplication : MultiDexApplication(), Application.ActivityLifecycleCallbacks {

    val LOG_FLAG = true
    private val publishSubject =
        PublishSubject.create<Pair<*, *>>()
    private var isInBackground = true
    private var applicationName: String? = null
    private var mContext: Context? = null

    private var baseApplication: BaseApplication? = null

    override fun onCreate() {
        super.onCreate()
      //  Fabric.with(this, Crashlytics())
        configImageLoader(this)
        baseApplication = this
        mContext = this
        applicationName = getApplicationName(this)
        //configureCalligraphyLibrary()

        // TODO: 12/20/2017 Enable it to use Calligraphy font library
//        configureCalligraphyLibrary();

        // TODO: 11/1/2017 Enable Crash Lytics and Never Crash feature before releasing the app
//        Fabric.with(this, new Crashlytics());
//        neverCrash();
        /*FirebaseInstanceId.getInstance().getInstanceId()
            .addOnCompleteListener({ task ->
                if (!task.isSuccessful() || task.getResult() == null) {
                    Log.w("FIREBASE", "getInstanceId failed", task.getException())
                    return@addOnCompleteListener
                }

                // Get new Instance ID token
                val token: String = task.getResult().getToken()

                // Log and toast
                println(token)
                SharedPreferenceManager.getInstance(getContext())
                    .putValue(KEY_FIREBASE_TOKEN, token)
                SharedPreferenceManager.getInstance(getContext())
                    .putValue(KEY_FIREBASE_TOKEN_UPDATED, true)
            })*/
    }

    fun getApp(): BaseApplication? {
        return baseApplication
    }

    fun getContext(): Context? {
        return mContext
    }


    private fun getApplicationName(context: Context): String? {
        val applicationInfo = context.applicationInfo
        val stringId = applicationInfo.labelRes
        return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(
            stringId
        )
    }

//    private void configureCalligraphyLibrary() {
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/SanFranciscoRegular.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//        );
//
//    }

    //    private void configureCalligraphyLibrary() {
    //        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
    //                .setDefaultFontPath("fonts/SanFranciscoRegular.ttf")
    //                .setFontAttrId(R.attr.fontPath)
    //                .build()
    //        );
    //
    //    }
    private fun configImageLoader(context: Context) {
        val defaultOptions =
            DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY).displayer(FadeInBitmapDisplayer(300))
                .build()
        // Create global configuration and initialize ImageLoaderHelper with this config
        val config = ImageLoaderConfiguration.Builder(context)
            .imageDownloader(CustomImageDownaloder(context))
            .defaultDisplayImageOptions(defaultOptions)
            .memoryCache(WeakMemoryCache())
            .memoryCacheSize(2 * 1024 * 1024)
            .build()
        ImageLoader.getInstance().init(config)
    }

    private fun configureCalligraphyLibrary() {
        /*ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        Builder()
                            .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )*/
    }

    /**
     * A method to perform a restart if crash appear, won't show crash to user and send the report to the Fabric
     */
    private fun neverCrash() {
        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable ->
            val t =
                Thread(Runnable { //                        SharedPreferenceManager.getInstance().setForcedRestart(true);
                  //  Crashlytics.logException(paramThrowable)
                })
            t.start()
            try {
                t.join()
            } catch (e: InterruptedException) {
                Log.e("CRASH", "uncaughtException: " + e.message)
            }

            //                Log.d("Crash BaseApplication", "uncaughtException: " + SharedPreferenceManager.getInstance().isForcedRestart());
            Log.e(
                "Error" + Thread.currentThread().stackTrace[2],
                paramThrowable.localizedMessage
            )
            val pendingIntent = PendingIntent.getActivity(
                applicationContext, 293, Intent(
                    applicationContext,
                    MainActivity::class.java
                ), 0
            )
            val mgr =
                getSystemService(Context.ALARM_SERVICE) as AlarmManager
            mgr[AlarmManager.RTC, System.currentTimeMillis() + 30] = pendingIntent
            System.exit(1)
        }
    }


    fun getPublishSubject(): PublishSubject<Pair<*, *>>? {
        return publishSubject
    }

    fun isInBackground(): Boolean {
        return isInBackground
    }

    override fun onActivityPaused(activity: Activity) {
        Log.e("abc", "onActivityPaused " + activity.toString())
    }

    override fun onActivityStarted(activity: Activity) {
        Log.e("abc", "onActivityStarted " + activity.toString() + "")
        if (isInBackground) {
            isInBackground = false
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.e("abc", "onActivityDestroyed " + activity.toString())
    }

    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
        Log.e("abc", "onActivityStopped " + activity.toString())

    }

    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
    }

    override fun onActivityResumed(activity: Activity) {
        if (isInBackground) {
            isInBackground = false
        }
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Log.e("abc", "onTrimMemory :- $level")
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            isInBackground = true
        }
    }

    fun getApplicationName(): String? {
        return applicationName
    }

}