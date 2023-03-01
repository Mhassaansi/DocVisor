package com.fictivestudios.docsvisor.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.constants.SELECT_USER
import com.fictivestudios.docsvisor.databinding.FragmentDoctorAlarmCalenderviewBinding
import com.fictivestudios.docsvisor.widget.TitleBar

class AlarmSetFragment : BaseFragment() {

    lateinit var binding: FragmentDoctorAlarmCalenderviewBinding
    lateinit var name: String

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
                R.layout.fragment_doctor_alarm_calenderview,
                container,
                false
            );

        binding.simpleCalendarView.minDate = System.currentTimeMillis() - 1000

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.setTitle("ALARM")
    }

    override fun setListeners() {

        binding.simpleCalendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->

            if (sharedPreferenceManager!!.getString(SELECT_USER).toString() == "Doctor") {
                val bottomSheet = BottomSheet.newInstance("Doctor",year.toString() + "-"+(month+1)+"-"+dayOfMonth)
                bottomSheet.show(getBaseActivity().supportFragmentManager, "BottomSheet")

            } else {
                val bottomSheet = BottomSheet.newInstance("Patient",year.toString() + "-"+(month+1)+"-"+dayOfMonth)
                bottomSheet.show(getBaseActivity().supportFragmentManager, "BottomSheet")
            }


        }
    }

    companion object {

        fun newInstance(): AlarmSetFragment {

            val args = Bundle()
            val fragment = AlarmSetFragment()
            fragment.arguments = args
            return fragment
        }
    }

}