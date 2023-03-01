package com.fictivestudios.docsvisor.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.ilhasoft.support.validation.Validator
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.HomeActivityPateint
import com.fictivestudios.docsvisor.adapter.CertificateAdapter2
import com.fictivestudios.docsvisor.adapter.DoctorListDescAdapter
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.databinding.FragmentDoctorListProfileBinding
import com.fictivestudios.docsvisor.helper.doctorData
import com.fictivestudios.docsvisor.widget.TitleBar
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_doctor_list_profile.*

class DoctorsProfileCertificateFragment : BaseFragment(), OnItemClickListener,
    OnChartValueSelectedListener {

    private var selectedImageAdapter: CertificateAdapter2? =null
    lateinit var binding: FragmentDoctorListProfileBinding
    var validator: Validator? = null
    private var arrayList: ArrayList<String>? = null
    private lateinit var adapter: DoctorListDescAdapter
    var certificateList: ArrayList<String>? = null

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
                R.layout.fragment_doctor_list_profile,
                container,
                false

            )

        setDoctorProfile()


        binding.btnReject.setOnClickListener {
            HomeActivityPateint.navControllerPatient!!.navigate(R.id.bookAppointmentFragment)
        }
/*
        binding.model = viewModel
        binding.lifecycleOwner = this*/
        return binding.root

        //  return inflater.inflate(R.layout.fragment_nearby_sessions, container, false);
    }

    private fun setDoctorProfile() {

        if (doctorData!=null)
        {
            binding.tvName.setText(doctorData?.name)
            binding.tvProfession.setText(doctorData?.profession)
            binding.tvEmail.setText(doctorData?.email)
            binding.tvBio.setText(doctorData?.bio)

            if (!doctorData?.image.isNullOrBlank())
            {
                Picasso.get().load(doctorData?.image).into(binding.ivImage)
            }
            if (doctorData?.certifications != null)
            {
                selectedImageAdapter = CertificateAdapter2(activity, doctorData?.certifications!! , this)
                binding.rvCer.adapter = selectedImageAdapter
                binding.rvCer.adapter?.notifyDataSetChanged()
            }

        }



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*    viewModel = ViewModelProvider.AndroidViewModelFactory(Application())
                .create<NearbySessionViewModel>(NearbySessionViewModel::class.java)*/
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arrayList = ArrayList()


        val mLayoutManager2: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        rvCer.layoutManager = mLayoutManager2
        (rvCer.itemAnimator as DefaultItemAnimator).supportsChangeAnimations =
            false



        /* rvCer.setLayoutManager(
             LinearLayoutManager(activity, RecyclerView.VERTICAL, false),
             R.layout.item_nearby_session_shimmer
         )*/
    }
    
    
    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showSidebar(getBaseActivity(), "Patient")
        titleBar?.setTitle("DOCTOR PROFILE")


    }

    override fun setListeners() {

    }

    companion object {

        fun newInstance(): DoctorsProfileCertificateFragment {

            val args = Bundle()

            val fragment = DoctorsProfileCertificateFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onItemClick(position: Int, `object`: Any?, view: View?, adapterName: String?) {
        if (doctorData != null) {


            val bundle = bundleOf(
                "certificateUrl" to doctorData?.certifications?.get(position)?.filename.toString())

            HomeActivityPateint.navControllerPatient!!.navigate(R.id.viewCertificateFragment,bundle)
        }


    }


    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            /* if (isLoading == null) return
             if (isLoading) {
                 rvCer?.showShimmer()
             } else {
                 rvCer?.hideShimmer()
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