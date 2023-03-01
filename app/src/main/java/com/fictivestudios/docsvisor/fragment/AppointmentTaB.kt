package com.fictivestudios.docsvisor.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.adapter.PagerAdapter
import com.fictivestudios.docsvisor.databinding.DashboardFragmentBinding
import com.fictivestudios.docsvisor.widget.TitleBar
import com.google.android.material.tabs.TabLayout

class AppointmentTaB : BaseFragment(), TabLayout.OnTabSelectedListener {

    private var binding: DashboardFragmentBinding? = null
    var tabTitle = arrayOf("Lists", "Requests")

    private fun prepareTabView(pos: Int): View? {
        val view: View = layoutInflater.inflate(R.layout.custom_tab, null)
        val tv_title = view.findViewById<View>(R.id.tv_title) as TextView
        tv_title.text = tabTitle[pos]
        return view
    }

     override fun getDrawerLockMode(): Int {
         return 0
     }

     override fun setTitlebar(titleBar: TitleBar?) {
         titleBar?.visibility = View.VISIBLE
         titleBar!!.setbtnrightgone()
         ///titleBar?.showSidebar(getBaseActivity(), "Patient")
         titleBar?.setTitle("Check Bound")
     }

     override fun setListeners() {
     }


     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding = DataBindingUtil.inflate(inflater, R.layout.dashboard, container, false)
         setupViewPager(binding!!.pager)
         return binding!!.root
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = PagerAdapter(childFragmentManager)
        adapter.addFragment(DoctorListFragment.newInstance(), "Lists")
        adapter.addFragment(RequestDoctorFragment.newInstance(), "Requests")
        viewPager.setAdapter(adapter)
        binding!!.tabLayout.setupWithViewPager(viewPager)
        setupTabIcons()
    }
    private fun setupTabIcons() {
        for (i in tabTitle.indices) {
            binding!!.tabLayout.getTabAt(i)!!.setCustomView(prepareTabView(i))
        }
    }

    companion object {

        fun newInstance(): AppointmentTaB {
            val args = Bundle()
            val fragment = AppointmentTaB()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }


}
