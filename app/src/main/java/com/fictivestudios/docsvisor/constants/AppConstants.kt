package com.fictivestudios.docsvisor.constants

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.fictivestudios.docsvisor.BaseApplication
import com.fictivestudios.docsvisor.helper.SharedPreferenceManager
import com.fictivestudios.docsvisor.libraries.ImageLoaderHelper
import java.text.SimpleDateFormat
import java.util.*


val JSON_STRING_KEY = "JSON_STRING_KEY"


var ROOT_PATH = (Environment.getExternalStorageDirectory().path
        + "/" + BaseApplication.getApplicationName())

val DOC_PATH = "$ROOT_PATH/Docs"

fun getUserFolderPath(context: Context?): String? {
    return "$DOC_PATH/" + SharedPreferenceManager.getInstance(context!!)?.getCurrentUser()
    //  .getUserDetails().getFirstName()
}

/*******************Preferences KEYS */
const val KEY_CURRENT_USER_MODEL = "userModel"
const val KEY_TOKEN = "getToken"
const val KEY_CURRENT_USER_ID = "user_model_id"
const val SELECT_USER = "user_patient_doctor"
const val LOGIN_TYPE = "login_type"
const val DOCTOR_BOTTOMNAV = "botton_nav"
const val DEVICE_OS_ANDROID = "android"
const val MODE = "dev"
const val SOCIAL_MEDIA_PLATFORM_GOOGLE = "google"
const val SOCIAL_MEDIA_PLATFORM_PHONE = "phone"
const val PUBLIC = "public"
const val PRIVATE = "private"
const val SOCIAL_MEDIA_PLATFORM_FACEBOOK = "facebook"
const val SOCIALLOGIN = "socialLogin"
const val LOGINSIMPLEORSOCIAL = "loginType"
var SETTINGTAB = 0

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String) {

    ImageLoaderHelper.loadImageWithoutAnimation(
        view,
        url,
        false
    )
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("date")
fun dateFormat(view: TextView, date: String) {
    if (date.isNullOrEmpty()) {
        view.text = ""
    } else {
        val dateFormatprev = SimpleDateFormat("yyyy-mm-dd hh:mm:ss")
        val d: Date = dateFormatprev.parse(date)
        val dateFormat = SimpleDateFormat("MMM dd, yyyy")
        val timeFormat = SimpleDateFormat("hh:mm aa")
        val changedDate: String = dateFormat.format(d)
        val changedTime: String = timeFormat.format(d)
        view.text = "$changedDate at $changedTime"
    }
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("course")
fun dateFormatCourseFragment(view: TextView, date: String) {
    if (date.isNullOrEmpty()) {
        view.text = ""
    } else {
        val dateFormatprev = SimpleDateFormat("yyyy-mm-dd")
        val d: Date = dateFormatprev.parse(date)
        val dateFormat = SimpleDateFormat("MMM dd, yyyy")
        val changedDate: String = dateFormat.format(d)
        view.text = changedDate
    }
}
