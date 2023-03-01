package com.fictivestudios.docsvisor.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.HomeActivity
import com.fictivestudios.docsvisor.adapter.FitnessAdapter
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.*
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.callbacks.OnSpinnerOKPressedListener
import com.fictivestudios.docsvisor.constants.Constants
import com.fictivestudios.docsvisor.databinding.FragmentDoctorPatientProfileBinding
import com.fictivestudios.docsvisor.helper.*
import com.fictivestudios.docsvisor.model.SpinnerModel
import com.fictivestudios.docsvisor.widget.TitleBar
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.Utils
import kotlinx.android.synthetic.main.fragment_doctor_patient_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.util.*
import kotlin.collections.HashMap


class DoctorPatientProfile : BaseFragment(), OnChartValueSelectedListener,OnItemClickListener {

    private var testCategory: String="heartRate"
    lateinit var binding: FragmentDoctorPatientProfileBinding
    lateinit var name: String

    var patientId = ""
    val PATIENT_ID="patientId"
    private var arrayList: List<FitnessData>? = null
    private lateinit var adapter: FitnessAdapter

    var patientProfileData:PatientProfileData? =null

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
                R.layout.fragment_doctor_patient_profile,
                container,
                false
            );

        arrayList = ArrayList()

        patientId = arguments?.getString(PATIENT_ID).toString()
        if (!patientId.isNullOrBlank() && patientId != "null")
        {
            PreferenceUtils.saveString(PATIENT_ID,patientId)
        }


        getPatientProfile(PreferenceUtils.getString(PATIENT_ID))

        binding.txtDiet.setOnClickListener {



            binding.txtDiet.background.setTint(resources.getColor(R.color.colorPrimary))
            binding.txtDiet.setTextColor(Color.parseColor("#ffffff"))
            binding.txtFitness.background.setTint(resources.getColor(R.color.white))
            binding.txtFitness.setTextColor(Color.parseColor("#6f64fe"))
            binding.txtMedics.background.setTint(resources.getColor(R.color.white))
            binding.txtMedics.setTextColor(Color.parseColor("#6f64fe"))

            FITNESS_TYPE = FITNESS_TYPE_DIET
            updateFitness(FITNESS_TYPE)
        }

        binding.txtFitness.setOnClickListener {



            binding.txtDiet.background.setTint(resources.getColor(R.color.white))
            binding.txtDiet.setTextColor(Color.parseColor("#6f64fe"))
            binding.txtFitness.background.setTint(resources.getColor(R.color.colorPrimary))
            binding.txtFitness.setTextColor(Color.parseColor("#ffffff"))
            binding.txtMedics.background.setTint(resources.getColor(R.color.white))
            binding.txtMedics.setTextColor(Color.parseColor("#6f64fe"))

            FITNESS_TYPE = FITNESS_TYPE_FITNESS
            updateFitness(FITNESS_TYPE)
        }

        binding.txtMedics.setOnClickListener {



            binding.txtDiet.background.setTint(resources.getColor(R.color.white))
            binding.txtDiet.setTextColor(Color.parseColor("#6f64fe"))
            binding.txtFitness.background.setTint(resources.getColor(R.color.white))
            binding.txtFitness.setTextColor(Color.parseColor("#6f64fe"))
            binding.txtMedics.background.setTint(resources.getColor(R.color.colorPrimary))
            binding.txtMedics.setTextColor(Color.parseColor("#ffffff"))
            FITNESS_TYPE = FITNESS_TYPE_MEDICS
            updateFitness(FITNESS_TYPE)
        }

        return binding.root
    }

    private fun updateFitness(fitnessType: String) {

        if (patientProfileData != null)
        {

            if(fitnessType== FITNESS_TYPE_MEDICS)
            {

                arrayList = patientProfileData!!.medics
                adapter = FitnessAdapter(requireActivity(), arrayList!!, this@DoctorPatientProfile,false)
                binding.rvEditFitness.adapter = adapter
                adapter.notifyDataSetChanged()
            }
            if(fitnessType== FITNESS_TYPE_DIET)
            {
                arrayList = patientProfileData!!.diet
                adapter = FitnessAdapter(requireActivity(), arrayList!!, this@DoctorPatientProfile,false)
                binding.rvEditFitness.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            if(fitnessType== FITNESS_TYPE_FITNESS)
            {
                arrayList = patientProfileData!!.fitness
                adapter = FitnessAdapter(requireActivity(), arrayList!!, this@DoctorPatientProfile,false)
                binding.rvEditFitness.adapter = adapter
                adapter.notifyDataSetChanged()
            }




        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtDiet.background.setTint(resources.getColor(R.color.colorPrimary))
        binding.txtDiet.setTextColor(Color.parseColor("#ffffff"))
        binding.txtFitness.background.setTint(resources.getColor(R.color.white))
        binding.txtFitness.setTextColor(Color.parseColor("#6f64fe"))
        binding.txtMedics.background.setTint(resources.getColor(R.color.white))
        binding.txtMedics.setTextColor(Color.parseColor("#6f64fe"))

        onBind()
    }


    private fun onBind() {
        run {
            // background color
            chart1.setBackgroundColor(Color.WHITE);

            // disable description text
            chart1.description.isEnabled = false;

            // enable touch gestures
            chart1.setTouchEnabled(true);

            // set listeners
            chart1.setOnChartValueSelectedListener(this);
            chart1.setDrawGridBackground(false);

            // create marker to display box when values are selected
            /*       MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

                   // Set the marker to the chart
               mv.setChartView(chart)
               chart1.setMarker(mv)*/

            // enable scaling and dragging
            chart1.setDragEnabled(true)
            chart1.setScaleEnabled(true)
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart1.setPinchZoom(false)
        }
        var xAxis: XAxis
        run {
            // // X-Axis Style // //
            xAxis = chart1.getXAxis()

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
            yAxis.axisMinimum = -50f
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
        val l: Legend = chart1.getLegend()

        // draw legend entries as lines
        l.setForm(Legend.LegendForm.LINE)
    }


    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE

        titleBar?.showBackButtonWithNotification(activity, "PATIENT PROFILE","Doctor")
    }

    override fun setListeners() {

        binding.btnTest.setOnClickListener {
            HomeActivity.navControllerHome.navigate(R.id.doctorPatientTestFragment)
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

                            getPatientProfile(PreferenceUtils.getString(PATIENT_ID))

                        }
                        else if (selectedPosition ==1)
                        {
                            testCategory = "highPressure"
                                    getPatientProfile(PreferenceUtils.getString(PATIENT_ID))
                        }
                        else if (selectedPosition ==2)
                        {
                            testCategory = "lowPressure"
                                    getPatientProfile(PreferenceUtils.getString(PATIENT_ID))
                        }
                        else if (selectedPosition ==3)
                        {
                            testCategory = "heartRate"
                                    getPatientProfile(PreferenceUtils.getString(PATIENT_ID))
                        }
                        else if (selectedPosition ==4)
                        {
                            testCategory = "temperature"
                                    getPatientProfile(PreferenceUtils.getString(PATIENT_ID))
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

        binding.txtDiet.setOnClickListener {
            binding.txtDiet.background.setTint(resources.getColor(R.color.colorPrimary))
            binding.txtDiet.setTextColor(Color.parseColor("#ffffff"))
            binding.txtFitness.background.setTint(resources.getColor(R.color.white))
            binding.txtFitness.setTextColor(Color.parseColor("#6f64fe"))
            binding.txtMedics.background.setTint(resources.getColor(R.color.white))
            binding.txtMedics.setTextColor(Color.parseColor("#6f64fe"))

            FITNESS_TYPE = FITNESS_TYPE_DIET
            updateFitness(FITNESS_TYPE)


        }

        binding.txtFitness.setOnClickListener {
            binding.txtDiet.background.setTint(resources.getColor(R.color.white))
            binding.txtDiet.setTextColor(Color.parseColor("#6f64fe"))
            binding.txtFitness.background.setTint(resources.getColor(R.color.colorPrimary))
            binding.txtFitness.setTextColor(Color.parseColor("#ffffff"))
            binding.txtMedics.background.setTint(resources.getColor(R.color.white))
            binding.txtMedics.setTextColor(Color.parseColor("#6f64fe"))

            FITNESS_TYPE = FITNESS_TYPE_FITNESS
            updateFitness(FITNESS_TYPE)

        }

        binding.txtMedics.setOnClickListener {
            binding.txtDiet.background.setTint(resources.getColor(R.color.white))
            binding.txtDiet.setTextColor(Color.parseColor("#6f64fe"))
            binding.txtFitness.background.setTint(resources.getColor(R.color.white))
            binding.txtFitness.setTextColor(Color.parseColor("#6f64fe"))
            binding.txtMedics.background.setTint(resources.getColor(R.color.colorPrimary))
            binding.txtMedics.setTextColor(Color.parseColor("#ffffff"))

            FITNESS_TYPE = FITNESS_TYPE_MEDICS
            updateFitness(FITNESS_TYPE)

        }



        binding.imgEdit.setOnClickListener {

            if (patientProfileData!=null)
            {
                patientFitnessdata = patientProfileData
                HomeActivity.navControllerHome.navigate(R.id.doctorEditPatientProfile)
            }

        }
    }


    private fun getPatientProfile(patientId: String)
    {


        binding.pbPFitness.visibility = View.VISIBLE
        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.getPatientProfile(patientId,testCategory).enqueue(object: retrofit2.Callback<GetPatientProfileResponse> {
                override fun onResponse(
                    call: Call<GetPatientProfileResponse>,
                    response: Response<GetPatientProfileResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbPFitness.visibility=View.GONE

                    })

                    Log.d("Response", response.message())

                    try {


                        if (response.isSuccessful) {

                            if (response.body()?.status==1)
                            {
                                Log.d("Response", response.body()!!.message)

                                Log.d("Response", response.body()!!.data.toString())

                                Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                                response.body()?.data?.tests?.let { setData(it) }

                                patientProfileData = response.body()?.data

                                arrayList = response.body()?.data?.diet
                                adapter = FitnessAdapter(requireActivity(), arrayList!!, this@DoctorPatientProfile,false)
                                binding.rvEditFitness.adapter = adapter
                                adapter.notifyDataSetChanged()


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
                        //Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<GetPatientProfileResponse>, t: Throwable)
                {

                     activity?.runOnUiThread(java.lang.Runnable {
                         Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                         binding.pbPFitness.visibility=View.GONE
                     })
                }
            })

        }


    }


    private fun setData(/*count: Int, range: Float*/data:HashMap<String,String>) {
        val values = ArrayList<Entry>()
        /*for (i in 0 until count) {
            val `val` = (Math.random() * range).toFloat() - 70
            values.add(Entry(i.toFloat(), `val`, resources.getDrawable(R.drawable.dropdown)))
        }*/

        for ((key,value) in data) {
            //val `val` = data[i].toFloat() //(Math.random() * range).toFloat() - 70
            values.add(Entry(key.toFloat(), value.toFloat(), resources.getDrawable(R.drawable.dropdown)))
        }

        val set1: LineDataSet
        if (chart1.getData() != null &&
            chart1.getData().getDataSetCount() > 0
        ) {
            set1 = chart1.getData().getDataSetByIndex(0) as LineDataSet
            set1.values = values
            set1.notifyDataSetChanged()
            chart1.data.notifyDataChanged()
            chart1.notifyDataSetChanged()
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
                IFillFormatter { dataSet, dataProvider -> chart1.getAxisLeft().getAxisMinimum() }

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                /*val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.menu_gradient)
                set1.fillDrawable = drawable*/
                set1.fillColor = Color.WHITE
            } else {
                set1.fillColor = Color.WHITE
            }
            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1) // add the data sets

            // create a data object with the data sets
            val data = LineData(dataSets)

            // set data

            chart1.setData(data)
            chart1.data.notifyDataChanged()
            chart1.notifyDataSetChanged()
            chart1.invalidate()
        }
    }


    companion object {

        fun newInstance(): DoctorPatientProfile {

            val args = Bundle()
            val fragment = DoctorPatientProfile()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }

    override fun onNothingSelected() {
    }

    override fun onItemClick(position: Int, `object`: Any?, view: View?, adapterName: String?) {

    }


}