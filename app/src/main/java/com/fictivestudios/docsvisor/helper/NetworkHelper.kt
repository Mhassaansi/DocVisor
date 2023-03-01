package com.fictivestudios.docsvisor.helper

import android.content.Context
import android.net.ConnectivityManager
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException


object NetworkHelper {




    // Call Back method  to get the Message form other Activity
    fun isNetworkConnected(context:Context): Boolean {
        val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }

    fun isHighAccuracy(context: Context): Boolean {
        try {
            val accuracy =
                Settings.Secure.getInt(context!!.contentResolver, Settings.Secure.LOCATION_MODE)
             if (accuracy == 3) {
                 return true
            } else {
                 return false
            }
        } catch (e: SettingNotFoundException) {
            e.printStackTrace()
        }
        return false
    }
}