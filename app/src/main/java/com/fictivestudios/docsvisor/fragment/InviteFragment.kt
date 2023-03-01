package com.fictivestudios.docsvisor.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.MainActivity
import com.fictivestudios.docsvisor.databinding.FragmentDoctorSettingBinding
import com.fictivestudios.docsvisor.databinding.FragmentPatientInviteDocBinding
import com.fictivestudios.docsvisor.databinding.FragmentSuccessBinding
import com.fictivestudios.docsvisor.widget.TitleBar

class InviteFragment : BaseFragment() {

    lateinit var binding: FragmentPatientInviteDocBinding
    lateinit var name: String

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_patient_invite_doc, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showBackButton(activity, "INVITE DOCTOR")
    }

    override fun setListeners() {

       /* binding.btnSuccess.setOnClickListener {

            getBaseActivity().finish()
            MainActivity.navController.navigate(R.id.homeActivity)
        }*/
    }

    companion object {

        fun newInstance(): InviteFragment {

            val args = Bundle()
            val fragment = InviteFragment()
            fragment.arguments = args
            return fragment
        }
    }


}