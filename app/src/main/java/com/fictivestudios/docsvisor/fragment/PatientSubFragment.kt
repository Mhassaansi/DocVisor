package com.fictivestudios.docsvisor.fragment

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.ilhasoft.support.validation.Validator
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.HomeActivity
import com.fictivestudios.docsvisor.activities.HomeActivityPateint
import com.fictivestudios.docsvisor.activities.MainActivity
import com.fictivestudios.docsvisor.adapter.DoctorPatientListAdapter
import com.fictivestudios.docsvisor.adapter.DoctorPatientSubsAdapter
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.constants.Constants
import com.fictivestudios.docsvisor.constants.DOCTOR_BOTTOMNAV
import com.fictivestudios.docsvisor.constants.SELECT_USER
import com.fictivestudios.docsvisor.databinding.FragmentDoctorPatientlistBinding
import com.fictivestudios.docsvisor.databinding.FragmentSubscribtionListBinding
import com.fictivestudios.docsvisor.model.DummyAdapterModel
import com.fictivestudios.docsvisor.widget.TitleBar
import com.google.android.material.button.MaterialButton
import com.google.gson.reflect.TypeToken
import com.todkars.shimmer.ShimmerAdapter
import kotlinx.android.synthetic.main.bottom_doctor_nav.*
import kotlinx.android.synthetic.main.fragment_doctor_patientlist.*
import kotlinx.android.synthetic.main.fragment_subscribtion_list.*

class PatientSubFragment : BaseFragment(), OnItemClickListener {

    lateinit var binding: FragmentSubscribtionListBinding

    // private lateinit var viewModel: NearbySessionViewModel
    var validator: Validator? = null
    private var arrayList: ArrayList<DummyAdapterModel>? = null
    private lateinit var nearbySessionAdapter: DoctorPatientSubsAdapter


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
                R.layout.fragment_subscribtion_list,
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

        arrayList = ArrayList()
        nearbySessionAdapter = DoctorPatientSubsAdapter(requireActivity(), arrayList!!, this)
        arrayList?.addAll(Constants.sampleList)
        val mLayoutManager2: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        subs.layoutManager = mLayoutManager2
        (subs.itemAnimator as DefaultItemAnimator).supportsChangeAnimations =
            false
        subs.adapter = nearbySessionAdapter
        nearbySessionAdapter.notifyDataSetChanged()
        /* subs.setLayoutManager(
             LinearLayoutManager(activity, RecyclerView.VERTICAL, false),
             R.layout.item_nearby_session_shimmer
         )
         subs.setItemViewType(ShimmerAdapter.ItemViewType { type: Int, position: Int -> R.layout.item_nearby_session_shimmer })*/

    }

    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showBackButton(activity, "SUBSCRIPTION")
    }

    override fun setListeners() {
       

    }

    companion object {

        fun newInstance(): PatientSubFragment {

            val args = Bundle()

            val fragment = PatientSubFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onItemClick(position: Int, `object`: Any?, v: View?, adapterName: String?) {
        when (v!!.id) {
            R.id.menu -> {
                val popup = PopupMenu(requireContext(), v!!)
                popup.inflate(R.menu.menu_patient)
                popup.setOnMenuItemClickListener(object :
                    MenuItem.OnMenuItemClickListener,
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem): Boolean {
                        when (item.itemId) {
                            R.id.menuChat -> {

                                HomeActivity.navControllerHome.navigate(R.id.messengerFragment)

                            }
                        }
                        return false
                    }
                })
                popup.show()
            }
            R.id.materialCardView -> {
                HomeActivity.navControllerHome.navigate(R.id.doctorPatientProfile)
            }
        }

    }


    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            if (isLoading == null) return
            /* if (isLoading) {
                 subs?.showShimmer()
             } else {
                 subs?.hideShimmer()
             }*/
        }
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