package com.fictivestudios.docsvisor.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.MainActivity
import com.fictivestudios.docsvisor.constants.SELECT_USER
import com.fictivestudios.docsvisor.databinding.FragmentSuccessBinding
import com.fictivestudios.docsvisor.widget.TitleBar

class SuccessSignupFragment : BaseFragment() {

    lateinit var binding: FragmentSuccessBinding
    lateinit var name: String

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_success, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.setTitle( "SIGNUP")
    }

    override fun setListeners() {

        binding.btnSuccess.setOnClickListener {

            if (sharedPreferenceManager?.getString(SELECT_USER).toString() == "Doctor") {
                getBaseActivity().finish()
                MainActivity.navController.navigate(R.id.homeActivity)
            } else {
                getBaseActivity().finish()
                MainActivity.navController.navigate(R.id.homeActivityPateint)
            }


        }
    }

    companion object {

        fun newInstance(): SuccessSignupFragment {

            val args = Bundle()
            val fragment = SuccessSignupFragment()
            fragment.arguments = args
            return fragment
        }
    }


}