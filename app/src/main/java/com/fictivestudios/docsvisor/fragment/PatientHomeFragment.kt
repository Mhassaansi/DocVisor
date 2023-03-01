package com.fictivestudios.docsvisor.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.DataBindingUtil
import br.com.ilhasoft.support.validation.Validator
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.HomeActivityPateint
import com.fictivestudios.docsvisor.activities.MainActivity
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.*
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.callbacks.OnSpinnerOKPressedListener
import com.fictivestudios.docsvisor.constants.Constants
import com.fictivestudios.docsvisor.constants.DOCTOR_BOTTOMNAV
import com.fictivestudios.docsvisor.databinding.FragmentPatientHomeBinding
import com.fictivestudios.docsvisor.helper.*
import com.fictivestudios.docsvisor.model.SpinnerModel
import com.fictivestudios.docsvisor.model.User
import com.fictivestudios.docsvisor.widget.TitleBar
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_patient_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PatientHomeFragment : BaseFragment(), OnItemClickListener,
    OnChartValueSelectedListener, DatePickerDialog.OnDateSetListener {

    lateinit var binding: FragmentPatientHomeBinding
    var validator: Validator? = null
    lateinit var dialog1:Dialog

    var dateArrayList = ArrayList<String>()

    var testType ="week"
    var testCategory = "bloodGlucose"
    var testGraphArray = ArrayList<TestResult>()
    var testGraphData:HashMap<String,String> = HashMap()


    var testUnit:String = "mg/dL"
    var userObj :User?= null
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
                R.layout.fragment_patient_home,
                container,
                false
            )

        var gson =  Gson()
        var json:String= PreferenceUtils.getString(USER_OBJECT)
        userObj=gson.fromJson(json, User::class.java)
        getGraphData(testType, testCategory, testUnit)
        //getGraphIndex(testType)
/*
        binding.model = viewModel
        binding.lifecycleOwner = this*/
        return binding.root

        //  return inflater.inflate(R.layout.fragment_nearby_sessions, container, false);
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferenceManager?.putValue(
            DOCTOR_BOTTOMNAV,
            "HomePatient"
        )
        HomeActivityPateint.home.patientHomeFragment()
        HomeActivityPateint.home.showBottomBar()
        onBind()
        onBindGraph2()
        binding.txtWeek.background.setTint(resources.getColor(R.color.colorPrimary))
        binding.txtWeek.setTextColor(Color.parseColor("#ffffff"))
        binding.txtMonth.background.setTint(resources.getColor(R.color.grey))
        binding.txtMonth.setTextColor(Color.parseColor("#6f64fe"))

        binding.txtWeek.setOnClickListener {
            binding.txtWeek.background.setTint(resources.getColor(R.color.colorPrimary))
            binding.txtWeek.setTextColor(Color.parseColor("#ffffff"))
            binding.txtMonth.background.setTint(resources.getColor(R.color.grey))
            binding.txtMonth.setTextColor(Color.parseColor("#6f64fe"))

            testType = TEST_TYPE_WEEK
            getGraphData(testType, testCategory, testUnit)
            //getGraphIndex(testType)

        }

        binding.txtMonth.setOnClickListener {
            binding.txtWeek.background.setTint(resources.getColor(R.color.grey))
            binding.txtWeek.setTextColor(Color.parseColor("#6f64fe"))
            binding.txtMonth.background.setTint(resources.getColor(R.color.colorPrimary))
            binding.txtMonth.setTextColor(Color.parseColor("#ffffff"))

            testType = TEST_TYPE_MONTH
            getGraphData(testType, testCategory, testUnit)
            //getGraphIndex(testType)

        }
    }

    private fun onBind() {
        chart.setUsePercentValues(true)
        chart.description.isEnabled = false
        chart.setExtraOffsets(5f, 10f, 5f, 5f)
        chart.dragDecelerationFrictionCoef = 0.95f
        chart.isDrawHoleEnabled = true
        chart.setHoleColor(Color.WHITE)
        chart.setTransparentCircleColor(Color.WHITE)
        chart.setTransparentCircleAlpha(110)
        chart.holeRadius = 58f
        chart.transparentCircleRadius = 61f
        chart.setDrawCenterText(true)
        chart.rotationAngle = 0f
        // enable rotation of the chart by touch
        chart.isRotationEnabled = false
        chart.isHighlightPerTapEnabled = true

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        chart.setOnChartValueSelectedListener(this)

        chart.animateY(1400, Easing.EaseInOutQuad)
        // chart.spin(2000, 0, 360);
        chart.legend.isEnabled = false
       /* val l: Legend = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f*/

        // entry label styling
        chart.setEntryLabelColor(Color.WHITE)
        chart.setEntryLabelTextSize(12f)
       // setData(2, 100F)
    }

    private fun onBindGraph2() {
        run {
            // background color
            chart1.setBackgroundColor(Color.WHITE)

            // disable description text
            chart1.description.isEnabled = false

            // enable touch gestures
            chart1.setTouchEnabled(true)

            // set listeners
            chart1.setOnChartValueSelectedListener(this)
            chart1.setDrawGridBackground(false)

            // create marker to display box when values are selected
            /*       MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

                   // Set the marker to the chart
               mv.setChartView(chart)
               chart1.setMarker(mv)*/

            // enable scaling and dragging
            chart1.isDragEnabled = true
            chart1.setScaleEnabled(true)
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart1.setPinchZoom(false)

            chart1.xAxis.labelCount = 3
/*            chart1.xAxis = 7f*/
        }
        var xAxis: XAxis
        run {
            // // X-Axis Style // //
            xAxis = chart1.xAxis

            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f)

        }
        var yAxis: YAxis
        run {
            // // Y-Axis Style // //
            yAxis = chart1.axisLeft

            // disable dual axis (only use LEFT axis)
            chart1.axisRight.isEnabled = false

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f)

            // axis range
            yAxis.axisMaximum = 200f
            yAxis.axisMinimum = 1f

        }
        run {
            // // Create Limit Lines // //
            /* val llXAxis = LimitLine(9f, "Index 10")
             llXAxis.lineWidth = 4f
             llXAxis.enableDashedLine(10f, 10f, 0f)
             llXAxis.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
             llXAxis.textSize = 10f
             val ll1 = LimitLine(150f, "Upper Limit")
             ll1.lineWidth = 4f
             ll1.enableDashedLine(10f, 10f, 0f)
             ll1.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
             ll1.textSize = 10f
             val ll2 = LimitLine(-30f, "Lower Limit")
             ll2.lineWidth = 4f
             ll2.enableDashedLine(10f, 10f, 0f)
             ll2.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
             ll2.textSize = 10f*/

            // draw limit lines behind data instead of on top
            yAxis.setDrawLimitLinesBehindData(true)
            xAxis.setDrawLimitLinesBehindData(true)

            // add limit lines
            /*  yAxis.addLimitLine(ll1)
              yAxis.addLimitLine(ll2)*/
        }



        // draw points over time
        chart1.animateX(1500)

        // get the legend (only possible after setting data)
        val l: Legend = chart1.legend

        // draw legend entries as lines
        l.form = Legend.LegendForm.LINE
    }

    private fun setData2(data: ArrayList<TestResult>, type:Int) {
        var values = ArrayList<Entry>()
       // values=values
        for (i in data) {
         //   val `val` = data[i].toFloat() //(Math.random() * range).toFloat() - 70
            values.add(Entry(i.key .toFloat(), i.value.toFloat(), resources.getDrawable(R.drawable.dropdown)))


        }
        val set1: LineDataSet
        if (chart1.data != null &&
            chart1.data.dataSetCount > 0
        ) {
            set1 = chart1.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values


            val xAxis: XAxis = binding.chart1.getXAxis()
           /* xAxis.mAxisMinimum = 1f
            xAxis.mAxisMaximum = type.toFloat()
            xAxis.axisMinimum = 1f
            xAxis.axisMaximum = type.toFloat()*/
            xAxis.setGranularity(1f);

            xAxis.valueFormatter = IndexAxisValueFormatter(dateArrayList)
            xAxis.labelCount = 3
/*
            xAxis.spaceMax = 5f
            xAxis.spaceMin = 5f

*/
                set1.notifyDataSetChanged()
            chart1.data.notifyDataChanged()
            chart1.notifyDataSetChanged()
            binding.chart1.resetZoom()
            chart1.invalidate()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values, "")
            set1.setDrawIcons(false)

            // draw dashed line
            // set1.enableDashedLine(10f, 5f, 0f)

            // black lines and points
            set1.color = Color.MAGENTA
            set1.setCircleColor(Color.MAGENTA)

            // line thickness and point size
            set1.lineWidth = 1f
            set1.circleRadius = 3f

            // draw points as solid circles
            set1.setDrawCircleHole(false)

            // customize legend entry
            set1.formLineWidth = 1f
            //set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f

            // text size of values
            set1.valueTextSize = 9f

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f)

            // set the filled area
            set1.setDrawFilled(true)
            set1.fillFormatter =
                IFillFormatter { dataSet, dataProvider -> chart1.axisLeft.axisMinimum }

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                /*val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.menu_gradient)
                set1.fillDrawable = drawable*/
                set1.fillColor = Color.WHITE
            } else {
                set1.fillColor = Color.WHITE
            }


            val xAxis: XAxis = binding.chart1.getXAxis()
            /* xAxis.mAxisMinimum = 1f
             xAxis.mAxisMaximum = type.toFloat()
             xAxis.axisMinimum = 1f
             xAxis.axisMaximum = type.toFloat()*/
            xAxis.setGranularity(1f);

            xAxis.valueFormatter = IndexAxisValueFormatter(dateArrayList)
            xAxis.labelCount = 3
    /*        xAxis.spaceMax = 5f
            xAxis.spaceMin = 5f
*/


            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1) // add the data sets

            // create a data object with the data sets
            val data = LineData(dataSets)

            // set data
            chart1.data = data
            chart1.invalidate()
        }
    }







    private fun setData(testGraphArray: ArrayList<TestResult>, type: Int) {
        val entries: java.util.ArrayList<PieEntry> = java.util.ArrayList<PieEntry>()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.

        for (i in testGraphArray) {
            //   val `val` = data[i].toFloat() //(Math.random() * range).toFloat() - 70
            entries.add(PieEntry(i.value.toFloat(), "", resources.getDrawable(R.drawable.dropdown)))
        }


/*        entries.add(
            PieEntry(
                (count.toFloat()),
                "",
                resources.getDrawable(R.drawable.dropdown)
            ))
        entries.add(
            PieEntry(
                (range),
                "",
                resources.getDrawable(R.drawable.dropdown)
            ))*/

        val dataSet = PieDataSet(entries, "")
        dataSet.setDrawIcons(false)
        //dataSet.setSliceSpace(3f)
        //dataSet.setIconsOffset(MPPointF(0, 40))
        dataSet.selectionShift = 5f

        // add a lot of colors
        val colors = java.util.ArrayList<Int>()
         /* colors.add(R.color.quantum_orange)
          colors.add(R.color.red)*/
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
/*         for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
         for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
         for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
         for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
         for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)*/
        // colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(chart))
        //data.setValueTextSize(11f)
        data.setValueTextColor(Color.TRANSPARENT)
        chart.data = data

        chart.description.isEnabled = false
        // undo all highlights
        chart.highlightValues(null)
        chart.invalidate()
    }

    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showSidebar(getBaseActivity(), "Patient")
        titleBar?.setTitle("HOME")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun setListeners() {

        binding.btnTest.setOnClickListener {
            getGraphData(testType, testCategory, testUnit)
        }

        symtoms.setOnClickListener {
            HomeActivityPateint.navControllerPatient!!.navigate(R.id.symptomFragment)

        }

        testManually.setOnClickListener {
             dialog1 = Dialog(requireContext())
            dialog1.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog1.setContentView(R.layout.dialog_test_manually)
            dialog1.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            dialog1.show()
            val txtYes = dialog1.findViewById<LinearLayoutCompat>(R.id.btnAccept)

            val tvHeartRate = dialog1.findViewById<AppCompatEditText>(R.id.tv_heart_rate)
            val tvGlucose = dialog1.findViewById<AppCompatEditText>(R.id.tv_glucose_level)
            val tvLowBlood = dialog1.findViewById<AppCompatEditText>(R.id.tv_low_pressure)
            val tvHighBlood = dialog1.findViewById<AppCompatEditText>(R.id.tv_high_pressure)
            val tvTemp = dialog1.findViewById<AppCompatEditText>(R.id.tv_temp)


            val cancel = dialog1.findViewById<AppCompatImageView>(R.id.cancel)
            val layoutClock = dialog1.findViewById<LinearLayoutCompat>(R.id.layoutClock)
            val date = dialog1.findViewById<AppCompatTextView>(R.id.date)
            val txtHour = dialog1.findViewById<AppCompatTextView>(R.id.txtHour)
            val txtMins = dialog1.findViewById<AppCompatTextView>(R.id.txtMins)
            val ampm = dialog1.findViewById<AppCompatTextView>(R.id.ampm)
            val c: Calendar = Calendar.getInstance()
            val year: Int = c.get(Calendar.YEAR)
            val month: Int = c.get(Calendar.MONTH)
            val day: Int = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    date.setText(year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth)
                    val c = Calendar.getInstance()
                    var mHour = c[Calendar.HOUR_OF_DAY]
                    var mMinute = c[Calendar.MINUTE]

                    // Launch Time Picker Dialog

                    // Launch Time Picker Dialog

                },
                year,
                month,
                day
            )
                    date.setOnClickListener {
                        datePickerDialog.show();
                    }
                    cancel.setOnClickListener {
                        dialog1.dismiss()
                    }
                    layoutClock.setOnClickListener {



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

                            }, hour, minute, false
                        ) //Yes 24 hour time
                        mTimePicker.setTitle("Select Time")
                        mTimePicker.show()
                    }

                    txtYes.setOnClickListener {
                        //dialog1.dismiss()

                        var hour = String.format("%02d",txtHour.text.toString().toInt())
                        var min =String.format("%02d", txtMins.text.toString().toInt())
                        var time = hour+":"+min+":00"


                        if (  !tvHeartRate.text.toString().isNullOrBlank()||
                        !tvGlucose.text.toString().isNullOrBlank()||
                        !tvHighBlood.text.toString().isNullOrBlank()||
                        !tvLowBlood.text.toString().isNullOrBlank()||
                        !tvTemp.text.toString().isNullOrBlank())
                        {

                            createTest(
                                date.text.toString(),
                                time,
                                tvHeartRate.text.toString(),
                                tvGlucose.text.toString(),
                                tvHighBlood.text.toString(),
                                tvLowBlood.text.toString(),
                                tvTemp.text.toString()

                            )
                        }
                        else
                        {
                            Toast.makeText(requireContext(), "please enter at least one test result", Toast.LENGTH_SHORT).show()
                        }

                    }
                }


        val spinnerSingleSelectDialogFragment =
            SpinnerDialogFragment.newInstance(
                "Please Select",
                Constants.arrTest,
                object :
                    OnSpinnerOKPressedListener {
                    override fun onSingleSelection(data: SpinnerModel, selectedPosition: Int) {
                        /*  binding!!.txtQualification.setTextColor(
                              ContextCompat.getColor(
                                  activity!!,
                                  R.color.black
                              )
                          )*/
                        binding.txtHeadingHeartRate.text = data.text
                        if (selectedPosition ==0)
                        {
                            testCategory = "bloodGlucose"
                            testUnit = "mg/dL"

                            getGraphData(testType,"bloodGlucose","mg/dL")

                        }
                        else if (selectedPosition ==1)
                        {
                            testCategory = "highPressure"
                            testUnit = "mmHg"


                            getGraphData(testType, "highPressure", "mmHg")
                        }
                        else if (selectedPosition ==2)
                        {
                            testCategory = "lowPressure"
                            testUnit = "mmHg"

                            getGraphData(testType, "lowPressure", "mmHg")
                        }
                        else if (selectedPosition ==3)
                        {
                            testCategory = "heartRate"
                            testUnit = "bpm"

                            getGraphData(testType, "heartRate", "bpm")
                        }
                        else if (selectedPosition ==4)
                        {
                            testCategory = "temperature"
                            testUnit = "°F"

                            getGraphData(testType, "temperature", "°F")
                        }


                    }
                },
                0
            )



        binding.relativeBgHeartRate.setOnClickListener {
            spinnerSingleSelectDialogFragment.show(
                requireActivity().supportFragmentManager,
                "SpinnerDialogFragmentSingle"
            )
        }


    }

    private fun createTest(
        date: String,
        time: String,
        tvHeartRate: String,
        tvGlucose: String,
        tvHighBlood: String,
        tvLowBlood: String,
        tvTemp: String
    ) {



        dialog1.findViewById<ProgressBar>(R.id.pb_test).visibility = View.VISIBLE

            val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())

            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)

                .addFormDataPart("start_time",date+" "+time)
                .addFormDataPart("heartRate", tvHeartRate)
                .addFormDataPart("bloodGlucose", tvGlucose)
                .addFormDataPart("highPressure", tvHighBlood)
                .addFormDataPart("lowPressure", tvLowBlood)
                .addFormDataPart("temperature", tvTemp)
                .addFormDataPart("watch_user_id", userObj?.watch_user_id.toString())
                .build()


            GlobalScope.launch(Dispatchers.IO)
            {

                apiClient.addTest(requestBody).enqueue(object: retrofit2.Callback<CommonResponse> {
                    override fun onResponse(
                        call: Call<CommonResponse>,
                        response: Response<CommonResponse>
                    )
                    {activity?.runOnUiThread(java.lang.Runnable {
                        dialog1.findViewById<ProgressBar>(R.id.pb_test).visibility = View.GONE

                    })
                        Log.d("Response", response.body()?.message.toString())

                        try {


                            if (response.isSuccessful) {

                                if (response.body()?.status == 1)
                                {
                                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                                    showSucces()
                                    dialog1.dismiss()

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
                        {activity?.runOnUiThread(java.lang.Runnable {
                            dialog1.findViewById<ProgressBar>(R.id.pb_test).visibility = View.GONE


                            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                        })

                        }
                    }

                    override fun onFailure(call: Call<CommonResponse>, t: Throwable)
                    {

                        activity?.runOnUiThread(java.lang.Runnable {
                            dialog1.findViewById<ProgressBar>(R.id.pb_test).visibility = View.GONE

                            Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                        })
                    }
                })




    }
    }

    private fun showSucces()
    {
        val dialog2 = Dialog(requireContext())
        dialog2.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog2.setContentView(R.layout.dialog_pateint_sugarlevel)
        dialog2.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog2.show()
        val txtYes = dialog2.findViewById<LinearLayoutCompat>(R.id.btnAccept)
        txtYes.setOnClickListener {
            dialog2.dismiss()

            HomeActivityPateint.navControllerPatient!!.popBackStack(
                R.id.patientHomeFragment,
                true
            )

        }
    }

    companion object {

        fun newInstance(): PatientHomeFragment {

            val args = Bundle()

            val fragment = PatientHomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onItemClick(position: Int, `object`: Any?, view: View?, adapterName: String?) {

    }




    override fun onValueSelected(e: Entry?, h: Highlight?) {
    }

    override fun onNothingSelected() {

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

    }

    private fun getGraphData(type: String, testCategory: String, testUnit: String)
    {
        binding.pbHomeP.visibility=View.VISIBLE

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.getGraphIndex(type, testCategory).enqueue(object: retrofit2.Callback<GraphResponse> {
                override fun onResponse(
                    call: Call<GraphResponse>,
                    response: Response<GraphResponse>
                )
                {

                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbHomeP.visibility=View.GONE


                    })

                    Log.d("Response", response.message())

                    try {

                        if (response.message() == "Unauthenticated." || response.message() == "Unauthorized")
                        {
                          PreferenceUtils.remove(USER_OBJECT)
                          PreferenceUtils.remove(ACCESS_TOKEN)
                            startActivity(Intent(requireContext(),MainActivity::class.java))
                            activity?.finish()
                            return
                        }

                        if (response.isSuccessful) {
                            Log.d("Response", response.body()!!.status.toString())
                            if (response.body()?.status==1)
                            {



                                Log.d("Response", response.body()!!.status.toString())

                                Log.d("Response", response.body()!!.data.toString())

                                Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()

                                if (response.body()?.data?.data != null)
                                {
                                    if (response?.body()?.data != null)
                                    {
                                        var responseData:GraphData = response?.body()?.data!!

                                        binding.ethigh.setText(responseData.high_test_count.toString())
                                        binding.ettotal.setText(responseData.total_test_count.toString())
                                        binding.etnormal.setText(responseData.normal_test_count.toString())
                                        binding.etaverage.setText(responseData.average_value.toString() +" "+ testUnit)
                                        binding.ethighest.setText(responseData.highest_value.toString())
                                        binding.etlowest.setText(responseData.lowest_value.toString())
                                        binding.tvUnitLowest.setText(testUnit)
                                        binding.tvUnitHighest.setText(testUnit)
                                        binding.etlow.setText(responseData.low_test_count.toString())


                                    }



                                    testGraphArray = response.body()?.data?.data!! as ArrayList<TestResult>
                                    if (type == TEST_TYPE_WEEK)
                                    {
                                        for (i in testGraphArray)
                                        {
                                            dateArrayList.add(i.date)
                                        }

                                        setData2(testGraphArray,7)
                                        setData(testGraphArray,7)
                                    }
                                    else
                                    {
                                        for (i in testGraphArray)
                                        {
                                            dateArrayList.add(i.date)
                                        }

                                        setData2(testGraphArray,30)
                                        setData(testGraphArray,30)
                                    }

                                }

                               /* if (response?.body()?.data != null)
                                {
                                    var responseData:GraphData = response?.body()?.data!!

                                    binding.ethigh.setText(responseData.high_range.toString())
                                    binding.ettotal.setText(responseData.total_test.toString())
                                    binding.etnormal.setText(responseData.normal_range.toString())
                                    binding.etaverage.setText(responseData.average_result.toString())
                                    binding.ethighest.setText(responseData.highest.toString())
                                    binding.etlowest.setText(responseData.lowest.toString())

                                  //   setData(responseData.highest.toInt(),responseData.average_result.toFloat())
                                }*/




                            }
                            else
                            {
                                activity?.runOnUiThread(java.lang.Runnable {
                                    Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_SHORT).show()

                                })

                            }

                        }
                        else {
                            activity?.runOnUiThread(java.lang.Runnable {
                                Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_SHORT).show()

                            })
                        }
                    }
                    catch (e:Exception)
                    {
                        activity?.runOnUiThread(java.lang.Runnable {
                            binding.pbHomeP.visibility=View.GONE
                            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
                            Log.d("error", e.message.toString())

                        })
                    }
                }

                override fun onFailure(call: Call<GraphResponse>, t: Throwable)
                {

                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbHomeP.visibility=View.GONE
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()

                    })
                }
            })

        }


    }

/*
    private fun getGraphIndex(type: String)
    {
        binding.pbHomeP.visibility=View.VISIBLE

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.getGraphData(type,"bloodGlucose").enqueue(object: retrofit2.Callback<GraphResponse> {
                override fun onResponse(
                    call: Call<GraphResponse>,
                    response: Response<GraphResponse>
                )
                {

                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbHomeP.visibility=View.GONE


                    })

                    Log.d("Response", response.message())

                    try {

                        if (response.message() == "Unauthenticated." || response.message() == "Unauthorized")
                        {
                            PreferenceUtils.remove(USER_OBJECT)
                            PreferenceUtils.remove(ACCESS_TOKEN)
                            startActivity(Intent(requireContext(),MainActivity::class.java))
                            activity?.finish()
                            return
                        }

                        if (response.isSuccessful) {
                            Log.d("Response", response.body()!!.status.toString())
                            if (response.body()?.status==1)
                            {



                                Log.d("Response", response.body()!!.status.toString())

                                Log.d("Response_average", response.body()!!.data.toString())

                                Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()

                     */
/*           if (!response.body()?.data?.test_results.isNullOrEmpty())
                                {
                                    testGraphData = response.body()?.data?.test_results!!

                                }*//*


                                if (response?.body()?.data != null)
                                {
                                    var responseData:GraphData = response?.body()?.data!!

                                    binding.ethigh.setText(responseData.high_test_count.toString())
                                    binding.ettotal.setText(responseData.total_test_count.toString())
                                    binding.etnormal.setText(responseData.normal_test_count.toString())
                                    binding.etaverage.setText(responseData.average_value.toString() +"mg/dL")
                                    binding.ethighest.setText(responseData.highest_value.toString())
                                    binding.etlowest.setText(responseData.lowest_value.toString())
                                    binding.etlow.setText(responseData.low_test_count.toString())

                                    setData(testGraphData)
                                }




                            }
                            else
                            {
                                activity?.runOnUiThread(java.lang.Runnable {
                                    Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_SHORT).show()

                                })

                            }

                        }
                        else {
                            activity?.runOnUiThread(java.lang.Runnable {
                                Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_SHORT).show()

                            })
                        }
                    }
                    catch (e:Exception)
                    {
                        activity?.runOnUiThread(java.lang.Runnable {
                            binding.pbHomeP.visibility=View.GONE
                            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
                            Log.d("error", e.message.toString())

                        })
                    }
                }

                override fun onFailure(call: Call<GraphResponse>, t: Throwable)
                {

                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbHomeP.visibility=View.GONE
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()

                    })
                }
            })

        }


    }
*/

}