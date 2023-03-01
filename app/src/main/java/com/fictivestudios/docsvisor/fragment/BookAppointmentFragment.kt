package com.fictivestudios.docsvisor.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.adapter.BookingAdapter
import com.fictivestudios.docsvisor.adapter.CertificateAdapter
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.BookAppointmentResponse
import com.fictivestudios.docsvisor.apiManager.response.Data
import com.fictivestudios.docsvisor.apiManager.response.ScheduleUserResponse
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.databinding.BookAppointbind
import com.fictivestudios.docsvisor.enums.FileType
import com.fictivestudios.docsvisor.helper.doctorData
import com.fictivestudios.docsvisor.widget.TitleBar
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_doctor_book_appoinment_calenderview.view.*
import kotlinx.android.synthetic.main.subcriptionlist.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class BookAppointmentFragment : BaseFragment(), OnItemClickListener, OnChartValueSelectedListener {


    lateinit var binding: BookAppointbind
    lateinit var name: String
    private var arrayList: ArrayList<Data>? = null
    private lateinit var adapter: BookingAdapter
    private var mYear:String? =null
    private var mMonth:String? =null
    private var mDayOfMonth:String? =null
    private var time:String?=null
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
                R.layout.fragment_doctor_book_appoinment_calenderview,
                container,
                false
            );


        binding.simpleCalendarView.minDate = System.currentTimeMillis() - 1000
        setDoctorProfile()

        return binding.root
    }

    private fun setDoctorProfile() {

        if (doctorData!=null)
        {
            binding.txtName.setText(doctorData?.name)
            if (!doctorData?.image.isNullOrBlank())
            {
                Picasso.get().load(doctorData?.image).into(binding.img)
            }
        }



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val mLayoutManager2: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvsubList.layoutManager = mLayoutManager2
        (rvsubList.itemAnimator as DefaultItemAnimator).supportsChangeAnimations =
            false




    }

    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.setTitle("BOOK APPOINTMENT")
    }


    private fun createAppointment(
        doctorID: String,
        date: String,
        time: String
    )
    {
        binding.pbAppoint.visibility=View.VISIBLE


        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)

            .addFormDataPart("start_time",time)
            .addFormDataPart("doctor_id", doctorID)
            .addFormDataPart("appoint_date", date)
            .build()


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.bookAppointment(requestBody).enqueue(object: retrofit2.Callback<BookAppointmentResponse> {
                override fun onResponse(
                    call: Call<BookAppointmentResponse>,
                    response: Response<BookAppointmentResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbAppoint.visibility=View.GONE

                    })

                    Log.d("Response", response.body()?.message.toString())

                    try {


                        if (response.code() == 1) {

                            Toast.makeText(context, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                            activity?.onBackPressed()

                        }
                        else {
                            Toast.makeText(context, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                    catch (e:Exception)
                    {
                        //Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<BookAppointmentResponse>, t: Throwable)
                {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                     activity?.runOnUiThread(java.lang.Runnable {
                         binding.pbAppoint.visibility=View.GONE


                     })
                }
            })

        }


    }

    private fun getSchedule(date:String)
    {

        binding.pbLoader.visibility=View.VISIBLE

        Log.d("date",date)

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())



        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.getSchedulePatient(doctorData?.id.toString(),date).enqueue(object: retrofit2.Callback<ScheduleUserResponse> {
                override fun onResponse(
                    call: Call<ScheduleUserResponse>,
                    response: Response<ScheduleUserResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbLoader.visibility=View.GONE

                    })

                    Log.d("scheduleResponse", response.message())

                    try {

                        val response: ScheduleUserResponse? =response.body()
                        val statuscode= response!!.status
                        if (statuscode==1) {

                            Log.d("scheduleResponse", response.message)
                            arrayList = ArrayList()
                            arrayList?.clear()

                            adapter = BookingAdapter(requireActivity(), arrayList!!,this@BookAppointmentFragment)
                            arrayList?.addAll(response.data)
                            rvsubList.adapter = adapter
                            adapter.notifyDataSetChanged()

                         }
                        else {
                            Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                            arrayList?.clear()
                            adapter.notifyDataSetChanged()
                        }
                    }
                    catch (e:Exception)
                    {
                        //Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ScheduleUserResponse>, t: Throwable)
                {

                     activity?.runOnUiThread(java.lang.Runnable {
                         binding.pbLoader.visibility=View.GONE
                         Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()

                     })
                }
            })

        }


    }

    override fun setListeners() {


        binding.root.btnReject.setOnClickListener {
/*
            HomeActivityPateint.navControllerPatient!!.navigate(R.id.appointmentTaB)
*/
/*            Log.d()*/
            if (doctorData != null && mYear!=null && !selectedTime.isNullOrEmpty())
            {

                createAppointment(doctorData?.id.toString(),"$mYear-$mMonth-$mDayOfMonth", selectedTime!!)
            }
        }

        binding.simpleCalendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->

            mYear = year.toString()
            mMonth = String.format("%02d", month+1)
            mDayOfMonth = String.format("%02d", dayOfMonth)

            getSchedule("$mYear-$mMonth-$mDayOfMonth")

//            if (sharedPreferenceManager!!.getString(SELECT_USER).toString() == "Doctor") {
//                val bottomSheet = BottomSheet.newInstance("Doctor")
//                bottomSheet.show(getBaseActivity().supportFragmentManager, "BottomSheet")
//
//            } else {
//                val bottomSheet = BottomSheet.newInstance("Patient")
//                bottomSheet.show(getBaseActivity().supportFragmentManager, "BottomSheet")
//            }


        }
    }



    companion object {

        var selectedTime:String?=null
        fun newInstance(): BookAppointmentFragment {

            val args = Bundle()
            val fragment = BookAppointmentFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onValueSelected(e: Entry?, h: Highlight?) {
    }

    override fun onNothingSelected() {
    }

    override fun onItemClick(position: Int, `object`: Any?, view: View?, adapterName: String?) {

        rvsubList.post(Runnable { adapter.notifyDataSetChanged() })

      // time = arrayList?.get(position)?.time
    }

}