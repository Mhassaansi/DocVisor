package com.fictivestudios.docsvisor.widget

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.BaseActivity
import com.fictivestudios.docsvisor.activities.HomeActivity
import com.fictivestudios.docsvisor.activities.HomeActivity.Companion.navControllerHome
import com.fictivestudios.docsvisor.activities.HomeActivityPateint
import com.fictivestudios.docsvisor.activities.HomeActivityPateint.Companion.navControllerPatient
import com.fictivestudios.docsvisor.activities.MainActivity
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.CommonResponse
import com.fictivestudios.docsvisor.constants.SELECT_USER
import com.fictivestudios.docsvisor.databinding.TitlebarBinding
import com.fictivestudios.docsvisor.helper.PreferenceUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.util.*


class TitleBar : RelativeLayout {

    private var binding: TitlebarBinding? = null

    constructor(context: Context) : super(context) {
        initLayout(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        initLayout(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        initLayout(context)
    }

    private fun initLayout(context: Context) {
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(inflater, R.layout.titlebar, this, true)
    }

    fun resetViews() {
        binding!!.btnLeft1.visibility = View.GONE
        binding!!.btnLeft2.visibility = View.GONE
        binding!!.btnRight1.visibility = View.GONE
        binding!!.btnRight2.visibility = View.GONE
        binding!!.txtTitle.visibility = View.GONE
        binding!!.btnLeftaudio.visibility = View.GONE
        binding!!.btnLeftvideo.visibility = View.GONE
        binding!!.btnRightaudio.visibility = View.GONE
        binding!!.btnRightvideo.visibility = View.GONE
        binding!!.containerTitlebar1.visibility = View.VISIBLE

    }

    fun setTitle(title: String?) {
        //resetViews()
        binding!!.txtTitle.text = title
        binding!!.txtTitle.visibility = View.VISIBLE
    }

    fun showBackButton(mActivity: Activity?, title: String?) {
        //resetViews()
        binding!!.btnLeft2.visibility = View.VISIBLE
        binding!!.btnRight1.visibility = View.INVISIBLE
        binding!!.txtTitle.text = title
        binding!!.txtTitle.visibility = View.VISIBLE
        binding!!.btnLeft2.setOnClickListener { mActivity?.onBackPressed() }
    }


    fun showBackButtonWithNotification(mActivity: Activity?, title: String?,frag: String?) {
        binding!!.btnLeft2.visibility = View.VISIBLE
        binding!!.btnRight1.visibility = View.VISIBLE
        binding!!.btnRight1.setOnClickListener {
            if (frag == "Doctor") {
                navControllerHome.navigate(R.id.notificationDoctorFragment)
            }else{
                navControllerPatient!!.navigate(R.id.notificationDoctorFragment2)
            }
        }
        binding!!.txtTitle.text = title
        binding!!.txtTitle.visibility = View.VISIBLE
        binding!!.btnLeft2.setOnClickListener { mActivity?.onBackPressed() }
    }


    fun setbtnrightgone() {
        binding!!.btnRight1!!.visibility = View.GONE
        binding!!.btnRight2!!.visibility = View.GONE
    }



    fun setbackbtn(mActivity: Activity?) {
        binding!!.btnLeft2.visibility = View.VISIBLE
        binding!!.btnLeft2.setOnClickListener {
            mActivity?.onBackPressed()
        }
    }
    fun setBtnRight(drawable: Int, listener: OnClickListener?) {
        //binding!!.btnRight1!!.visibility = View.GONE
        binding!!.btnRight1!!.setBackgroundResource(drawable)
        binding!!.btnRight1!!.setOnClickListener(listener)
        binding!!.btnRight1!!.visibility = View.VISIBLE
    }

    private fun dailog() {

        var fromDate=""
        var toDate=""

        val dialog1 = Dialog(context)
        dialog1.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog1.setContentView(R.layout.dateandtimedailog)
        dialog1.setCancelable(false)
        dialog1.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog1.show()
        val txtYes = dialog1.findViewById<LinearLayoutCompat>(R.id.btnAccept)
        val cancel = dialog1.findViewById<AppCompatImageView>(R.id.cancel)
        val from = dialog1.findViewById<AppCompatTextView>(R.id.date)
        val to = dialog1.findViewById<AppCompatEditText>(R.id.tv_to)

        cancel.setOnClickListener { dialog1.dismiss() }
        txtYes.setOnClickListener {

            requestAddOffDays(fromDate,toDate,context)
            dialog1.dismiss()
        }

        from.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox

                from.setText("" +year  + "-" + String.format("%02d",(monthOfYear+1)) + "-" + String.format("%02d", dayOfMonth))
                fromDate = (year.toString()  + "-" + String.format("%02d",(monthOfYear+1)) + "-" + String.format("%02d", dayOfMonth))
            }, year, month, day)

            dpd.show()
        }
        to.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                to.setText(year.toString()  + "-" + String.format("%02d",(monthOfYear+1)) + "-" + String.format("%02d", dayOfMonth))
                toDate = (year.toString()  + "-" + String.format("%02d",(monthOfYear+1)) + "-" + String.format("%02d", dayOfMonth))
            }, year, month, day)

            dpd.show()
        }


    }

    private fun requestAddOffDays(fromDate: String, toDate: String, context: Context)
    {




        val apiClient = ApiClient.RetrofitInstance.getApiService(context)

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)

            .addFormDataPart("from_date",fromDate)
            .addFormDataPart("to_date", toDate)
            .build()


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.addOffDays(requestBody).enqueue(object: retrofit2.Callback<CommonResponse> {
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

                    Log.d("Response", response.body()?.message.toString())

                    try {


                        if (response.isSuccessful) {

                            if (response.body()?.status == 1)
                            {
                                Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()



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
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CommonResponse>, t: Throwable)
                {
                
                }
            })




        }

    }

    fun showBackButtonWithAddButton(mActivity: Activity?, title: String?, frag: String?) {
        binding!!.btnRight2.setOnClickListener {
            if (frag == "Doctor") {
                navControllerHome.navigate(R.id.alarmSetFragment)

            } else if(frag == "OFFDAYS") {

                dailog()

            }else{
                navControllerPatient!!.navigate(R.id.alarmSetFragment2)
            }

        }

        binding!!.btnLeft2.visibility = View.VISIBLE
        binding!!.btnRight2.visibility = View.VISIBLE
        binding!!.txtTitle.text = title
        binding!!.txtTitle.visibility = View.VISIBLE
        binding!!.btnLeft2.setOnClickListener { mActivity?.onBackPressed() }
    }

    fun showBackButtonChat(mActivity: Activity?, title: String?,recieverId: String?, frag: String?) {

        val bundle = bundleOf(
            "receiverUserId" to recieverId,
            "name" to title
            )

        binding!!.btnLeftaudio.visibility = View.INVISIBLE
        binding!!.btnLeftvideo.visibility = View.INVISIBLE
        binding!!.btnRightaudio.visibility = View.VISIBLE
        binding!!.btnRightvideo.visibility = View.VISIBLE
        binding!!.btnLeft2.visibility = View.VISIBLE
        binding!!.btnRight2.visibility = View.INVISIBLE
        binding!!.txtTitle.text = title
        binding!!.txtTitle.visibility = View.VISIBLE


        if (frag == "Patient")
        {
            binding!!.btnRightaudio.visibility = View.INVISIBLE
            binding!!.btnRightvideo.visibility = View.INVISIBLE
        }
        else
        {

            binding!!.btnRightaudio.visibility = View.VISIBLE
            binding!!.btnRightvideo.visibility = View.VISIBLE
        }

        binding!!.btnRightaudio.setOnClickListener {




            if (frag == "Doctor") {
                HomeActivity.navControllerHome.navigate(R.id.audioFragment, bundle)
            } else {
                navControllerPatient!!.navigate(R.id.audioFragment2,bundle)
            }

        }
        binding!!.btnRightvideo.setOnClickListener {
            if (frag == "Doctor") {
                HomeActivity.navControllerHome.navigate(R.id.videoFragment,bundle)
            } else {
                navControllerPatient!!.navigate(R.id.videoFragment2,bundle)
            }
        }
        binding!!.btnLeft2.setOnClickListener {

            try{
                if (frag == "Doctor") {
                    HomeActivity.navControllerHome .navigate(R.id.chatlistDoctorFragment, bundle)
                } else {
                    navControllerPatient?.navigate(R.id.chatlistDoctorFragment2,bundle)
                }


            }
            catch (e:Exception)
            {
                Log.d("chat exception",e.localizedMessage.toString())
            }


        }
    }


    fun showSidebarKeyboard(onClickListener: OnClickListener) {
        binding!!.btnRight1.visibility = View.VISIBLE
        binding!!.btnLeft1.visibility = View.VISIBLE
        binding!!.btnLeft1.setOnClickListener(onClickListener)
    }


    fun showSidebar(mActivity: BaseActivity?, frag: String?) {
        binding!!.btnRight1.visibility = View.VISIBLE
        binding!!.btnRight1.setOnClickListener {
            if (frag == "Doctor") {
                navControllerHome.navigate(R.id.notificationDoctorFragment)
            }else{
                navControllerPatient!!.navigate(R.id.notificationDoctorFragment2)
            }
        }
        binding!!.btnLeft1.visibility = View.VISIBLE
        binding!!.btnLeft1.setOnClickListener {
            if (mActivity != null) {
                if (frag == "Doctor") {
                    if (HomeActivity.navControllerHome != null) {

                        if (navControllerHome.previousBackStackEntry != null)
                            navControllerHome.popBackStack(
                                navControllerHome.previousBackStackEntry!!.id,
                                true
                            )
                        BaseActivity.getDrawerLayout(mActivity)?.openDrawer(GravityCompat.START)
                    }
                } else {
                    if (navControllerPatient != null) {

                        if (navControllerPatient!!.previousBackStackEntry != null)
                            navControllerPatient!!.popBackStack(
                                navControllerPatient!!.previousBackStackEntry!!.id,
                                true
                            )
                        BaseActivity.getDrawerLayout(mActivity)?.openDrawer(GravityCompat.START)
                    }
                }

            }
        }
    }

    fun hideBottomBar(activity: Activity)
    {

        if (activity is HomeActivityPateint) {
            activity.hideBottomBar()
        }
        else
        {
            (activity as HomeActivity).hideBottomBar()
        }
    }

}