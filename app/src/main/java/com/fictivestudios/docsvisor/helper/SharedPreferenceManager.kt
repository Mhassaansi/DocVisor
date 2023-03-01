package com.fictivestudios.docsvisor.helper

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.fictivestudios.docsvisor.constants.KEY_CURRENT_USER_MODEL
import com.fictivestudios.docsvisor.model.UserModel
import com.google.gson.Gson


object SharedPreferenceManager {
    private var pref: SharedPreferences? = null
    private var factory: SharedPreferenceManager? = null

    @JvmStatic
    fun getInstance(context: Context): SharedPreferenceManager? {
        if (pref == null) pref = context.getSharedPreferences("mypref", Context.MODE_PRIVATE)
        if (factory == null) factory =
            SharedPreferenceManager
        return factory
    }

    fun clearDB() {
        // Save Registered Device Data
        //  val token = getString(KEY_FIREBASE_TOKEN)
        pref!!.edit().clear().commit()
        //  putObject(AppConstants.KEY_FIREBASE_TOKEN, token)*/
    }

    fun clearKey(key: String?) {
        pref!!.edit().remove(key).commit()
    }


    fun putValue(key: String?, value: Any?) {
        if (value is Boolean) pref!!.edit().putBoolean(key, (value as Boolean?)!!)
            .commit() else if (value is String) pref!!.edit()
            .putString(key, value as String?).commit() else if (value is Int) pref!!.edit()
            .putInt(key, value).commit() else if (value is Long) pref!!.edit()
            .putLong(key, value).commit()

//        else
//            pref.edit().putString(key, (String) value).apply();
    }

    fun getInt(key: String?): Int {
        return pref!!.getInt(key, -1)
    }

    fun getLong(key: String?): Long {
        return pref!!.getLong(key, -1)
    }

    fun getString(key: String?): String? {
        return pref!!.getString(key, "")
    }

    fun getBoolean(key: String?): Boolean {
        return pref!!.getBoolean(key, false)
    }

    fun putObject(key: String?, `object`: Any?) {
        if (`object` == null || `object` == "") {
            pref!!.edit().putString(key, `object` as String?).commit()
            return
        }
        pref!!.edit().putString(key, Gson().toJson(`object`)).commit()
    }

    fun removeObject(key: String?) {
        pref!!.edit().remove(key).commit()
    }

    fun <T> getObject(key: String, a: Class<T>?): T? {
        val json = pref!!.getString(key, null)
        return if (json == null) {
            null
        } else {
            try {
                Gson().fromJson(json, a)
            } catch (e: Exception) {
                throw IllegalArgumentException(
                    "Object stored with key "
                            + key + " is instance of other class"
                )
            }
        }
    }

    fun hasValue(key: String?): Boolean {
        return pref!!.contains(key)
    }

    fun getCurrentUser(): UserModel? {
        return getObject<UserModel>(KEY_CURRENT_USER_MODEL, UserModel::class.java)
    }

    /* fun isForcedRestart(): Boolean {
         return getBoolean(FORCED_RESTART)
     }

     fun setForcedRestart(isForcedRestart: Boolean) {
         putValue(FORCED_RESTART, isForcedRestart)
     }
 */
    fun removePreference(
        context: Context, prefsName: String?,
        key: String?
    ) {
        val preferences: SharedPreferences = context.getSharedPreferences(
            prefsName, Activity.MODE_PRIVATE
        )
        val editor = preferences.edit()
        editor.remove(key)
        editor.commit()
    }


}
