package com.fictivestudios.docsvisor.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.ilhasoft.support.validation.Validator
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.HomeActivityPateint
import com.fictivestudios.docsvisor.adapter.DoctorPatientTestAdapter
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.Report
import com.fictivestudios.docsvisor.apiManager.response.TestHistoryResponse
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.callbacks.OnSpinnerOKPressedListener
import com.fictivestudios.docsvisor.constants.Constants
import com.fictivestudios.docsvisor.constants.DOCTOR_BOTTOMNAV
import com.fictivestudios.docsvisor.databinding.FragmentPateintHistoryBinding
import com.fictivestudios.docsvisor.model.SpinnerModel
import com.fictivestudios.docsvisor.widget.TitleBar
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.Utils
import kotlinx.android.synthetic.main.fragment_doctor_list.*
import kotlinx.android.synthetic.main.fragment_pateint_history.*
import kotlinx.android.synthetic.main.fragment_pateint_history.chart1
import kotlinx.android.synthetic.main.fragment_patient_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class PatientHistoryFragment : BaseFragment(), OnItemClickListener,
    OnChartValueSelectedListener {

    lateinit var binding: FragmentPateintHistoryBinding
    var validator: Validator? = null
    private var graphArrayList: HashMap<String,String> = HashMap()

    private lateinit var adapter: DoctorPatientTestAdapter


//    var testType ="week"
    var testCategory = "heartRate"
    private var daysArray:List<Report> = ArrayList<Report>()
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
                R.layout.fragment_pateint_history,
                container,
                false
            )

        getTestHistory(testCategory)
/*
        binding.model = viewModel
        binding.lifecycleOwner = this*/
        return binding.root

        //  return inflater.inflate(R.layout.fragment_nearby_sessions, container, false);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*    viewModel = ViewModelProvider.AndroidViewModelFactory(Application())
                .create<NearbySessionViewModel>(NearbySessionViewModel::class.java)*/
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferenceManager?.putValue(
            DOCTOR_BOTTOMNAV,
            "HistoryPatient"
        )
        HomeActivityPateint.home.patientHistory()
        HomeActivityPateint.home.showBottomBar()
        onBind()

        val mLayoutManager2: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvReport.layoutManager = mLayoutManager2
        (rvReport.itemAnimator as DefaultItemAnimator).supportsChangeAnimations =
            false

        /* rvReport.setLayoutManager(
             LinearLayoutManager(activity, RecyclerView.VERTICAL, false),
             R.layout.item_nearby_session_shimmer
         )
         rvReport.setItemViewType(ShimmerAdapter.ItemViewType { type: Int, position: Int -> R.layout.item_nearby_session_shimmer })
 */
    }

    private fun onBind() {
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

/*        setData2(45, 180f)*/

        // draw points over time
        chart1.animateX(1500)

        // get the legend (only possible after setting data)
        val l: Legend = chart1.legend

        // draw legend entries as lines
        l.form = Legend.LegendForm.LINE
    }
    private fun setData2(data: HashMap<String, String>) {
        val values = java.util.ArrayList<Entry>()
        for ((key,value ) in data) {

            values.add(Entry(key.toFloat(), value.toFloat(), resources.getDrawable(R.drawable.dropdown)))
        }
        val set1: LineDataSet
        if (chart1.data != null &&
            chart1.data.dataSetCount > 0
        ) {
            set1 = chart1.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values



            val xAxis: XAxis = binding.chart1.getXAxis()
            xAxis.mAxisMinimum = 1f
            xAxis.mAxisMaximum = 7f
            xAxis.axisMinimum = 1f
            xAxis.axisMaximum = 7f

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
            val dataSets = java.util.ArrayList<ILineDataSet>()
            dataSets.add(set1) // add the data sets

            // create a data object with the data sets
            val data = LineData(dataSets)

            // set data
            chart1.data = data
            chart1.invalidate()
        }
    }


    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showSidebar(getBaseActivity(), "Patient")
        titleBar?.setTitle("HISTORY")
    }

    override fun setListeners() {


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
                        if (data.positionInList ==0)
                        {
                            testCategory = "bloodGlucose"

                            getTestHistory(testCategory)

                        }
                        else if (data.positionInList ==1)
                        {
                            testCategory = "highPressure"
                            getTestHistory(testCategory)
                        }
                        else if (data.positionInList ==2)
                        {
                            testCategory = "lowPressure"
                            getTestHistory(testCategory)
                        }
                        else if (data.positionInList ==3)
                        {
                            testCategory = "heartRate"
                            getTestHistory(testCategory)
                        }
                        else if (data.positionInList ==4)
                        {
                            testCategory = "temperature"
                            getTestHistory(testCategory)
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

    companion object {

        fun newInstance(): PatientHistoryFragment {

            val args = Bundle()

            val fragment = PatientHistoryFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onItemClick(position: Int, `object`: Any?, view: View?, adapterName: String?) {

    }

    private fun getTestHistory(category: String)
    {
        binding.pbHistory.visibility=View.VISIBLE

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.getTestHistory(category).enqueue(object: retrofit2.Callback<TestHistoryResponse> {
                override fun onResponse(
                    call: Call<TestHistoryResponse>,
                    response: Response<TestHistoryResponse>
                )
                {

                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbHistory.visibility=View.GONE


                    })

                    Log.d("Response", response.message())

                    try {



                        if (response.isSuccessful) {
                            Log.d("Response", response.body()!!.status.toString())
                            if (response.body()?.status==1)
                            {
                                Log.d("Response", response.body()!!.status.toString())

                                Log.d("Response", response.body()!!.data.toString())

                                Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()

                                if (!response.body()?.data?.tests.isNullOrEmpty())
                                {
                                    graphArrayList = response.body()?.data?.tests!!
                                    setData2(graphArrayList)

                                }
                                if (!response.body()?.data?.report.isNullOrEmpty())
                                {
                                    daysArray = response.body()?.data?.report!!
                                    adapter = DoctorPatientTestAdapter( requireActivity(),daysArray!!, this@PatientHistoryFragment)
                                    binding.rvReport.adapter = adapter
                                    adapter.notifyDataSetChanged()

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
                            binding.pbHistory.visibility=View.GONE
                            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
                            Log.d("error", e.message.toString())

                        })
                    }
                }

                override fun onFailure(call: Call<TestHistoryResponse>, t: Throwable)
                {

                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbHistory.visibility=View.GONE
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()

                    })
                }
            })

        }


    }

    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            /* if (isLoading == null) return
             if (isLoading) {
                 rvReport?.showShimmer()
             } else {
                 rvReport?.hideShimmer()
             }*/
        }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
    }

    override fun onNothingSelected() {

    }

}