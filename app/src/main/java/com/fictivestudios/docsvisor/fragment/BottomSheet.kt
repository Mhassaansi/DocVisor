package com.fictivestudios.docsvisor.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.HomeActivity
import com.fictivestudios.docsvisor.activities.HomeActivityPateint
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.AlarmResponse
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_clock_dialog.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.util.*

class BottomSheet : BottomSheetDialogFragment() {


    var typeFrag = ""
    var date = ""
    private fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    companion object {

        fun newInstance(s: String,date:String): BottomSheet {

            val args = Bundle()
            val fragment = BottomSheet()
            fragment.typeFrag = s
            fragment.date = date
            return fragment
        }
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        //Set the custom view
        val view: View = LayoutInflater.from(context).inflate(R.layout.fragment_clock_dialog, null)
        dialog.setContentView(view)

        val params = (view.parent as View).layoutParams as CoordinatorLayout.LayoutParams

        // this is for full screen height
        // this is for full screen height
        params.height = getScreenHeight()

        val behavior = params.behavior
        if (behavior is BottomSheetBehavior<*>) {
            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                @SuppressLint("SwitchIntDef")
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    var state = ""
                    when (newState) {
                        BottomSheetBehavior.STATE_DRAGGING -> {
                            state = "DRAGGING"
                        }
                        BottomSheetBehavior.STATE_SETTLING -> {
                            state = "SETTLING"
                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            state = "EXPANDED"
                        }
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            state = "COLLAPSED"
                        }
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            dismiss()
                            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            state = "HIDDEN"
                        }
                    }
                    //                    Toast.makeText(getContext(), "Bottom Sheet State Changed to: " + state, Toast.LENGTH_SHORT).show();
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_clock_dialog, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()

    }




    private fun setListener() {
        layoutClock.setOnClickListener { // TODO Auto-generated method stub
            val mcurrentTime: Calendar = Calendar.getInstance()
            val hour: Int = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute: Int = mcurrentTime.get(Calendar.MINUTE)
            val mTimePicker: TimePickerDialog = TimePickerDialog(
                requireContext(),
                { timePicker, selectedHour, selectedMinute ->
                    txtHour.text = selectedHour.toString()
                    txtMins.text = selectedMinute.toString()
                    val am_pm = if (selectedHour < 12) "AM" else "PM"
                    ampm.text = am_pm
                }, hour, minute, true
            ) //Yes 24 hour time
            mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        }

        btnApply.setOnClickListener {

            if (!txtHour.text.toString().isNullOrBlank()&&
                !txtMins.text.toString().isNullOrBlank()&&
                !tv_note.text.toString().isNullOrBlank())
            {
                createAlarm(txtHour.text.toString(),txtMins.text.toString(),tv_note.text.toString())
            }
            else
            {
                Toast.makeText(requireContext(), "please select time and note", Toast.LENGTH_SHORT).show()
            }



        }
    }




    private fun createAlarm(hour: String, mins: String, note: String)
    {


        var mMin = String.format("%02d", mins.toInt())
        var mHour = String.format("%02d", hour.toInt())

        pb_alarm.visibility = View.VISIBLE

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)

            .addFormDataPart("alaram_time","$mHour:$mMin:00")
            .addFormDataPart("alaram_date", date)
            .addFormDataPart("note", note)
            .build()


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.addAlarm(requestBody).enqueue(object: retrofit2.Callback<AlarmResponse> {
                override fun onResponse(
                    call: Call<AlarmResponse>,
                    response: Response<AlarmResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        pb_alarm.visibility=View.GONE


                    })

                    Log.d("Response", response.body()?.message.toString())

                    try {


                        if (response.isSuccessful) {
                            Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                            if (response.body()?.status == 1)
                            {

                                showDialogues()
                            }

                        }
                        else {
                            Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    catch (e:Exception)
                    {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AlarmResponse>, t: Throwable)
                {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                     activity?.runOnUiThread(java.lang.Runnable {
                         pb_alarm.visibility=View.GONE

                     })
                }
            })

        }


    }

    private fun showDialogues()
    {
        val dialog1 = Dialog(requireContext())
        dialog1.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog1.setContentView(R.layout.dialog_allarmset)
        dialog1.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog1.show()
        val txtYes = dialog1.findViewById<LinearLayoutCompat>(R.id.btnAccept)
        txtYes.setOnClickListener {
            dialog1.dismiss()
            dismiss()
            if (typeFrag == "Doctor") {
                HomeActivity.navControllerHome.popBackStack(R.id.alarmSetFragment, true)

            } else {
                HomeActivityPateint.navControllerPatient!!.popBackStack(
                    R.id.alarmSetFragment2,
                    true
                )

            }
        }
    }


}