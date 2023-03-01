package com.fictivestudios.docsvisor.fragment

import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.ilhasoft.support.validation.Validator
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.HomeActivity
import com.fictivestudios.docsvisor.adapter.DaysAdapter
import com.fictivestudios.docsvisor.adapter.SubscriptionAdapter
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.AddScheduleResponse
import com.fictivestudios.docsvisor.apiManager.response.GetSchedule
import com.fictivestudios.docsvisor.apiManager.response.ScheduleData
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.databinding.Weekbind
import com.fictivestudios.docsvisor.widget.TitleBar
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class WeeklySchedule  : BaseFragment(), OnItemClickListener,
    OnChartValueSelectedListener {

    lateinit var binding: Weekbind
    var validator: Validator? = null
    private var daysList: List<ScheduleData>? = null
    private lateinit var adapter: SubscriptionAdapter

    lateinit var from:TextView
    lateinit var to:EditText
    lateinit var daytitle:AppCompatTextView
    var dayString:String?=null
    var dialog1 :Dialog?=null

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.schedule,
                container,
                false
            )


        binding.doyouwanrtoaddoffdays.setOnClickListener {
            HomeActivity.navControllerHome.navigate(R.id.offDayAndTime2)

        }


        getScheduleRequest()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }


    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        ///  titleBar?.showSidebar(getBaseActivity(), "Patient")
        titleBar?.setTitle("Weekly Schedule")
        titleBar?.setbackbtn(activity)
    }

    override fun setListeners() {

    }

    companion object {
        fun newInstance(): WeeklySchedule {
            val args = Bundle()
            val fragment = WeeklySchedule()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onItemClick(position: Int, `object`: Any?, view: View?, adapterName: String?) {


        daysList?.get(position)?.day?.let { dailog(it) }
      //  HomeActivityPateint.navControllerPatient.navigate(R.id.doctorsProfileCertificateFragment)

    }

    private fun  dailog(day: String) {
       // val dialog1 = Dialog(getBaseActivity())
        dialog1 = Dialog(getBaseActivity())
        dialog1!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog1!!.setContentView(R.layout.daysdailog)
        dialog1!!.setCancelable(true)
        dialog1!!.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog1!!.show()
        val txtYes = dialog1!!.findViewById<LinearLayoutCompat>(R.id.btnAccept)
         from = dialog1!!.findViewById<AppCompatTextView>(R.id.date)
         to = dialog1!!.findViewById<AppCompatEditText>(R.id.to_date)
        daytitle = dialog1!!.findViewById<AppCompatTextView>(R.id.tv_day_title)


        daytitle.text = day

        from.setOnClickListener {







            val c: Calendar = Calendar.getInstance()
            var mHour = c[Calendar.HOUR_OF_DAY]
            var mMinute = c[Calendar.MINUTE]


            val timePickerDialog = TimePickerDialog(requireContext(),
                OnTimeSetListener { view, hourOfDay, minute, ->

                   var mHours = String.format("%02d", hourOfDay)
                    var mMin = String.format("%02d", minute)
                      from.setText("$mHours:$mMin:00")
                                  },
                mHour,
                mMinute,

                false
            )
            timePickerDialog.show()


        }

        to.setOnClickListener {


            val c: Calendar = Calendar.getInstance()
            var mHour = c[Calendar.HOUR_OF_DAY]
            var mMinute = c[Calendar.MINUTE]

            val timePickerDialog = TimePickerDialog(requireContext(),
                OnTimeSetListener { view, hourOfDay, minute ->
                    var mHours = String.format("%02d", hourOfDay)
                    var mMin = String.format("%02d", minute)
                    to.setText("$mHours:$mMin:00") },
                mHour,
                mMinute,
                false
            )
            timePickerDialog.show()

        }

        txtYes.setOnClickListener {
            addScheduleRequest()
             }


    }

    private fun addScheduleRequest()
    {
        binding.pbSchedule.visibility=View.VISIBLE

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)

            .addFormDataPart("day",daytitle.text.toString())
            .addFormDataPart("from_time", from.text.toString())
            .addFormDataPart("to_time",to.text.toString())
            .build()


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.addSchedule(requestBody).enqueue(object: retrofit2.Callback<AddScheduleResponse> {
                override fun onResponse(
                    call: Call<AddScheduleResponse>,
                    response: Response<AddScheduleResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbSchedule.visibility=View.GONE

                    })

                    Log.d("scheduleResponse", response.message())

                    try {

                        val response: AddScheduleResponse? =response.body()
                        val statuscode= response!!.status
                        if (statuscode==1) {

                            Log.d("scheduleResponse", response.message)

                            dialog1!!.dismiss()
                            getScheduleRequest()
                        }
                        else {
                            Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    catch (e:Exception)
                    {
                        //Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AddScheduleResponse>, t: Throwable)
                {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbSchedule.visibility=View.GONE

                    })
                }
            })

        }


    }


    private fun getScheduleRequest()
    {

        binding.pbSchedule.visibility=View.VISIBLE

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.getScheduleDoctor().enqueue(object: retrofit2.Callback<GetSchedule> {
                override fun onResponse(
                    call: Call<GetSchedule>,
                    response: Response<GetSchedule>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbSchedule.visibility=View.GONE

                    })

                    Log.d("scheduleResponse", response.message())
                    Log.d("scheduleResponse", response.body()?.message.toString())
                    try {

                        val response: GetSchedule? =response.body()
                        val statuscode= response!!.status
                        if (statuscode==1) {

                            Log.d("scheduleResponse", response.message)

                            if (!response.data.isNullOrEmpty())
                            {
                                daysList = ArrayList<ScheduleData>()
                                daysList = response.data
                               var adapter =   DaysAdapter(requireActivity(), daysList!!, this@WeeklySchedule)
                                binding.rvDays.adapter = adapter
                                adapter.notifyDataSetChanged()

                            }
                        }
                        else {
                            Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    catch (e:Exception)
                    {
                        //Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                        Log.d("scheduleResponse", e.localizedMessage)

                    }
                }

                override fun onFailure(call: Call<GetSchedule>, t: Throwable)
                {

                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbSchedule.visibility=View.GONE

                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    })
                    /* activity?.runOnUiThread(java.lang.Runnable {
                         binding.pbChangePass.visibility=View.GONE
                         binding.btnSignUp.isEnabled=true
                         binding.btnSignUp.text="RESET"

                     })*/
                }
            })

        }


    }


    private fun datePickerDiaogue()
    {
/*        val c: Calendar = Calendar.getInstance()
       var mYear = c.get(Calendar.YEAR)
       var mMonth = c.get(Calendar.MONTH)
       var mDay = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(requireContext(),
            OnDateSetListener { view, year, monthOfYear, dayOfMonth -> txtDate.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year) },
            mYear,
            mMonth,
            mDay
        )
        datePickerDialog.show()*/

    }
    private fun clockPickerDialogue()
    {
        // Get Current Time
        // Get Current Time
/*        val c = Calendar.getInstance()
        var mHour = c[Calendar.HOUR_OF_DAY]
        var mMinute = c[Calendar.MINUTE]

        // Launch Time Picker Dialog

        // Launch Time Picker Dialog
        val timePickerDialog = TimePickerDialog(requireContext(),
            OnTimeSetListener { view, hourOfDay, minute -> txtTime.setText("$hourOfDay:$minute") },
            mHour,
            mMinute,
            false
        )
        timePickerDialog.show()*/
    }

    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            /* if (isLoading == null) return
             if (isLoading) {
                 rvDocList?.showShimmer()
             } else {
                 rvDocList?.hideShimmer()
             }*/
        }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
    }

    override fun onNothingSelected() {

    }


    /* internal inner class WebResponseObserver : Observer<WebResponse<Any>> {
         override fun onChanged(webResponse: WebResponse<Any>) {
             val type =
                 object : TypeToken<java.util.ArrayList<NearbyReceivingModel>?>() {}.type
             val arr: java.util.ArrayList<NearbyReceivingModel> = GsonFactory.getSimpleGson()
                 .fromJson(
                     GsonFactory.getSimpleGson()
                         .toJson(webResponse.result), type
                 )
             arrayList?.clear()
             arrayList?.addAll(arr)
             nearbySessionAdapter.notifyDataSetChanged()
         }
     }
 */
}