package com.fictivestudios.docsvisor.fragment

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
import com.fictivestudios.docsvisor.activities.HomeActivityPateint
import com.fictivestudios.docsvisor.adapter.AppointmentListadapter
import com.fictivestudios.docsvisor.adapter.SubscriptionAdapter
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.constants.Constants
import com.fictivestudios.docsvisor.constants.SELECT_USER
import com.fictivestudios.docsvisor.databinding.Doctorlist
import com.fictivestudios.docsvisor.databinding.Subscriptionlist
import com.fictivestudios.docsvisor.widget.TitleBar
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.fragment_doctor_list.*
import kotlinx.android.synthetic.main.fragment_doctor_list.rvDocList
import kotlinx.android.synthetic.main.subcriptionlist.*

class Subscription : BaseFragment(), OnItemClickListener,
    OnChartValueSelectedListener {

    lateinit var binding: Subscriptionlist
    var validator: Validator? = null
    private var arrayList: ArrayList<String>? = null
    private lateinit var adapter: SubscriptionAdapter

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
                R.layout.subcriptionlist,
                container,
                false
            )

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arrayList = ArrayList()
        adapter = SubscriptionAdapter(requireActivity(), arrayList!!, this)
        arrayList?.addAll(Constants.arrDaysss)

        val mLayoutManager2: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvsubList.layoutManager = mLayoutManager2
        (rvsubList.itemAnimator as DefaultItemAnimator).supportsChangeAnimations =
            false
        rvsubList.adapter = adapter
        adapter.notifyDataSetChanged()

    }


    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE

        if (sharedPreferenceManager?.getString(SELECT_USER).toString() == "Doctor") {
            titleBar?.showSidebar(getBaseActivity(), "Doctor")
        } else {
            titleBar?.showSidebar(getBaseActivity(), "Patient")

        }
        titleBar?.setTitle("Subscription")
    }

    override fun setListeners() {

    }

    companion object {
        fun newInstance(): Subscription {
            val args = Bundle()
            val fragment = Subscription()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onItemClick(position: Int, `object`: Any?, view: View?, adapterName: String?) {
        HomeActivityPateint.navControllerPatient!!.navigate(R.id.doctorsProfileCertificateFragment)

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