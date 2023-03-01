//package com.fictivestudios.docsvisor.fragment
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.databinding.DataBindingUtil
//import com.fictivestudios.docsvisor.R
//import com.fictivestudios.docsvisor.constants.SELECT_USER
//import com.fictivestudios.docsvisor.databinding.FragmentDoctorAlarmCalenderviewBinding
//import com.fictivestudios.docsvisor.widget.TitleBar
//
//class ApoinmentSetFragment : BaseFragment() {
//
//    lateinit var binding: FragmentDoctorBookAppoinmentCalenderviewBinding
//
//    override fun getDrawerLockMode(): Int {
//        return 0
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//
//        binding =
//            DataBindingUtil.inflate(
//                inflater,
//                R.layout.fragment_doctor_book_appoinment_calenderview,
//                container,
//                false
//            );
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//    }
//
//    override fun setTitlebar(titleBar: TitleBar?) {
//        titleBar?.visibility = View.VISIBLE
//        titleBar?.setTitle("ALARM")
//    }
//
//    override fun setListeners() {
//
//        binding.simpleCalendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
//
//            if (sharedPreferenceManager!!.getString(SELECT_USER).toString() == "Doctor") {
//                val bottomSheet = BottomSheetAppointment.newInstance("Doctor")
//                bottomSheet.show(getBaseActivity().supportFragmentManager, "BottomSheet")
//
//            } else {
//                val bottomSheet = BottomSheetAppointment.newInstance("Patient")
//                bottomSheet.show(getBaseActivity().supportFragmentManager, "BottomSheet")
//            }
//
//
//        }
//    }
//
//    companion object {
//
//        fun newInstance(): ApoinmentSetFragment {
//
//            val args = Bundle()
//            val fragment = ApoinmentSetFragment()
//            fragment.arguments = args
//            return fragment
//        }
//    }
//
//}