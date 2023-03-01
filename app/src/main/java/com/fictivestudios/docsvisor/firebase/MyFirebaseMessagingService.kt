package com.fictivestudios.docsvisor.firebase

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavDeepLinkBuilder
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.HomeActivity
import com.fictivestudios.docsvisor.activities.HomeActivityPateint
import com.fictivestudios.docsvisor.activities.MainActivity
import com.fictivestudios.docsvisor.model.PushNotification
import com.fictivestudios.docsvisor.constants.SELECT_USER
import com.fictivestudios.docsvisor.enums.FCMEnums
import com.fictivestudios.docsvisor.helper.*
import com.fictivestudios.docsvisor.model.PushAgoraData
import com.fictivestudios.docsvisor.model.User
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private var broadcaster: LocalBroadcastManager? = null
    val CHANNEL_ID = "mychannel"

    override fun onCreate() {
        super.onCreate()
        broadcaster = LocalBroadcastManager.getInstance(applicationContext) 
    }
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("firebaseMessage",message.data .toString())

        Log.d("firebaseMessage",message.data.get("type").toString())

        PreferenceUtils.init(this)


            if (message.data.get("type").toString() ==  "video" )
            {
                val agoraData = PushAgoraData(
                    message.data.get("channel").toString(),
                    message.data.get("type").toString(),
                    message.data.get("user").toString(),
                    message.data.get("token").toString(),
                    message.data.get("user_name").toString(),



                )
                Log.e("pushObject", agoraData.toString())
                sendAgoraNotification(agoraData, "video")

            }
            else if (message.data.get("type").toString() ==  "audio")
            {
                val agoraData = PushAgoraData(
                    message.data.get("channel").toString(),
                    message.data.get("type").toString(),
                    message.data.get("user").toString(),
                    message.data.get("token").toString(),
                    message.data.get("user_name").toString()

                )
                Log.e("pushObject", agoraData.toString())
                sendAgoraNotification(agoraData,"audio")
            }

            else if (message.data.get("type").toString() ==  "call_reject")
            {


                rejectCall()
            }


            else
            {
                val push = PushNotification(
                    message.data.get("body").toString(),
                    message.data.get("postId").toString(),
                    message.data.get("title").toString(),
                    message.data.get("type").toString(),

                    )

                Log.e("pushObject", push.toString())
                sendNotification(push)

            }



    }

    private fun rejectCall() {

 /*       val bundle = bundleOf(
            REJECT_CALL to REJECT_CALL
        )*/
//        getDoctorPendingIntent(destinationId = R.id.messengerFragment,bundle)



        val intent = Intent("MyData")
        intent.putExtra(REJECT_CALL, REJECT_CALL)
        broadcaster?.sendBroadcast(intent);
/*
        if (isAppRunning())
        {




        }*/
    }

    private fun sendAgoraNotification(push: PushAgoraData, type: String) {

        var pendingIntent: PendingIntent? = null


        var activity = HomeActivity::class.java

        val bundle = bundleOf(
            "token" to push.token
            ,"channel" to push.channel
            ,"name" to push.user_name
            ,"type" to push.type
            ,"user" to push.user
        )



        if (SharedPreferenceManager.getInstance(this)?.getString(SELECT_USER) == "Doctor"
        )

        {
            /*pendingIntent = when(push.type){
                FCMEnums.VIDEO .value -> getDoctorPendingIntent(destinationId = R.id.audioFragment,bundle)
                FCMEnums.AUDIO .value -> getDoctorPendingIntent(destinationId = R.id.videoFragment,bundle)


                else -> null
            }*/
        }
        else
        {
            pendingIntent = when(push.type){

                FCMEnums.VIDEO .value -> getPatientPendingIntent(destinationId = R.id.callerScreenFragment,bundle)
                FCMEnums.AUDIO .value -> getPatientPendingIntent(destinationId = R.id.callerScreenFragment,bundle)
                else -> null
            }
        }







        val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        val notificationBuilder = NotificationCompat.Builder(applicationContext, packageName)
            .setSmallIcon(R.drawable.notifications)
            .setContentTitle(type +" Call from: "+push.user_name )
            .setContentText(type+" Call")
            .setAutoCancel(true)
            .setSound(alarmSound)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_CALL)

//            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.not))
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            //.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(packageName, push.user_name, NotificationManager.IMPORTANCE_HIGH).apply {
                description = ""
                enableVibration(true)
                setShowBadge(true)
                /*   val audioAttributes = AudioAttributes.Builder()
                       .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                       .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                       .build()
                   setSound(alarmSound,audioAttributes)*/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    setAllowBubbles(true)
                }
            }
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("firebaseMessage",token.toString())
        PreferenceUtils.saveString(FCM,token.toString())
    }




    private fun sendNotification(push: PushNotification) {

        var pendingIntent: PendingIntent? = null
        if ( SharedPreferenceManager.getInstance(this)?.getString(SELECT_USER) == "Doctor"
        )

        {
             pendingIntent = when(push.type){
                FCMEnums.CHAT .value -> {
                    val bundle = bundleOf("receiverUserId" to push.postId
                        ,"username" to push.title)
                    getDoctorPendingIntent(destinationId = R.id.messengerFragment,bundle)
                }
                FCMEnums.APPOINTMENT .value -> getDoctorPendingIntent(destinationId = R.id.appointmentTaB)

                FCMEnums.ALARM .value -> getDoctorPendingIntent(destinationId = R.id.alarmDoctorFragment)
                else -> null
            }
        }
        else
        {
            pendingIntent = when(push.type){
                FCMEnums.CHAT .value -> {
                    val bundle = bundleOf("receiverUserId" to push.postId
                        ,"username" to push.title)
                    getPatientPendingIntent(destinationId = R.id.messengerFragment2,bundle)
                }
                FCMEnums.APPOINTMENT .value -> getPatientPendingIntent(destinationId = R.id.notificationDoctorFragment2)

                FCMEnums.ALARM .value -> getPatientPendingIntent(destinationId = R.id.alarmDoctorFragment2)
                else -> null
            }
        }








        val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(applicationContext, packageName)
            .setSmallIcon(R.drawable.notifications)
            .setContentTitle(push.title ?: "title")
            .setContentText(push.body ?: "body")
            .setAutoCancel(true)
            .setSound(alarmSound)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)

//            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.not))
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
           .setContentIntent(pendingIntent)


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(packageName, push.title, NotificationManager.IMPORTANCE_HIGH).apply {
                description = push.body
                enableVibration(true)
                setShowBadge(true)
                /*   val audioAttributes = AudioAttributes.Builder()
                       .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                       .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                       .build()
                   setSound(alarmSound,audioAttributes)*/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    setAllowBubbles(true)
                }
            }
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }



    private fun getPatientPendingIntent(destinationId: Int, bundle: Bundle? = null): PendingIntent =
        NavDeepLinkBuilder(applicationContext)
            .setComponentName(HomeActivityPateint::class.java)
            .setGraph(R.navigation.pateint_module_graph)
            .setDestination(destinationId)
            .setArguments(bundle)
            .createPendingIntent()

    /**
     * Pending Intent Handle
     * */
        private fun getDoctorPendingIntent(destinationId: Int, bundle: Bundle? = null): PendingIntent =
        NavDeepLinkBuilder(applicationContext)
            .setComponentName(HomeActivity::class.java)
            .setGraph(R.navigation.doctor_module_graph)
            .setDestination(destinationId)
            .setArguments(bundle)
            .createPendingIntent()


    fun isAppRunning() : Boolean {

        val services = (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).runningAppProcesses
        return services.firstOrNull{it.processName.equals("com.fictivestudios.docsvisor",true)} != null
    }

}