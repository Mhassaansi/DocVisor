package com.fictivestudios.docsvisor.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.ilhasoft.support.validation.Validator
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.adapter.DoctorPatientTestAdapter
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.callbacks.OnSpinnerOKPressedListener
import com.fictivestudios.docsvisor.constants.Constants
import com.fictivestudios.docsvisor.databinding.FragmentDoctorPatientTestBinding
import com.fictivestudios.docsvisor.model.SpinnerModel
import com.fictivestudios.docsvisor.widget.TitleBar
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_doctor_patient_test.*

class DoctorPatientTestFragment : BaseFragment(), OnItemClickListener,
    OnChartValueSelectedListener {

    lateinit var binding: FragmentDoctorPatientTestBinding
    var validator: Validator? = null
    private var arrayList: ArrayList<String>? = null
    private lateinit var adapter: DoctorPatientTestAdapter

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
                R.layout.fragment_doctor_patient_test,
                container,
                false
            )

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

        onBind()
        arrayList = ArrayList()
       /* adapter = DoctorPatientTestAdapter(requireActivity(), arrayList!!, this)
        arrayList?.addAll(Constants.arrDaysss)
        val mLayoutManager2: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvReport.layoutManager = mLayoutManager2
        (rvReport.itemAnimator as DefaultItemAnimator).supportsChangeAnimations =
            false
        rvReport.adapter = adapter
        adapter.notifyDataSetChanged()*/
        /* rvReport.setLayoutManager(
             LinearLayoutManager(activity, RecyclerView.VERTICAL, false),
             R.layout.item_nearby_session_shimmer
         )
         rvReport.setItemViewType(ShimmerAdapter.ItemViewType { type: Int, position: Int -> R.layout.item_nearby_session_shimmer })
 */
    }

    private fun onBind() {
        chart.setUsePercentValues(true)
        chart.getDescription().setEnabled(false)
        chart.setExtraOffsets(5f, 10f, 5f, 5f)
        chart.setDragDecelerationFrictionCoef(0.95f)
        chart.setDrawHoleEnabled(true)
        chart.setHoleColor(Color.WHITE)
        chart.setTransparentCircleColor(Color.WHITE)
        chart.setTransparentCircleAlpha(110)
        chart.setHoleRadius(58f)
        chart.setTransparentCircleRadius(61f)
        chart.setDrawCenterText(true)
        chart.setRotationAngle(0f)
        // enable rotation of the chart by touch
        chart.setRotationEnabled(false)
        chart.setHighlightPerTapEnabled(true)

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        chart.setOnChartValueSelectedListener(this)

        chart.animateY(1400, Easing.EaseInOutQuad)
        // chart.spin(2000, 0, 360);
        val l: Legend = chart.getLegend()
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
        l.setOrientation(Legend.LegendOrientation.VERTICAL)
        l.setDrawInside(false)
        l.setXEntrySpace(7f)
        l.setYEntrySpace(0f)
        l.setYOffset(0f)

        // entry label styling
        chart.setEntryLabelColor(Color.WHITE)
        chart.setEntryLabelTextSize(12f)
        setData(2, 100F)
    }

    private fun setData(count: Int, range: Float) {
        val entries: java.util.ArrayList<PieEntry> = java.util.ArrayList<PieEntry>()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (i in 0 until count) {
            entries.add(
                PieEntry(
                    (Math.random() * range + range / 5).toFloat(),
                    "",
                    getResources().getDrawable(R.drawable.dropdown)
                )
            )
        }
        val dataSet = PieDataSet(entries, "")
        dataSet.setDrawIcons(false)
        //dataSet.setSliceSpace(3f)
        //dataSet.setIconsOffset(MPPointF(0, 40))
        dataSet.selectionShift = 5f

        // add a lot of colors
        val colors = java.util.ArrayList<Int>()
        /*  colors.add(R.color.quantum_orange)
          colors.add(R.color.red)*/
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        /* for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
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

        // undo all highlights
        chart.highlightValues(null)
        chart.invalidate()
    }

    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showBackButtonWithNotification(activity, "PATIENT TESTS", "Doctor")
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

        fun newInstance(): DoctorPatientTestFragment {

            val args = Bundle()

            val fragment = DoctorPatientTestFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onItemClick(position: Int, `object`: Any?, view: View?, adapterName: String?) {

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