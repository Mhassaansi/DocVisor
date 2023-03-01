package com.fictivestudios.docsvisor.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.MainActivity
import com.fictivestudios.docsvisor.databinding.FragmentPreLoginBinding
import com.fictivestudios.docsvisor.databinding.FragmentSelectUserBinding
import com.fictivestudios.docsvisor.databinding.FragmentSignupBinding
import com.fictivestudios.docsvisor.widget.TitleBar
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

class CompleteFragment : BaseFragment() {

    lateinit var binding: FragmentSignupBinding
    lateinit var name: String

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_doctor_profile, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showBackButton(activity, "SIGNUP")
    }

    override fun setListeners() {

        binding.btnSignUp.setOnClickListener {

            MainActivity.navController.navigate(R.id.verificationFragment)
        }
    }

    companion object {

        fun newInstance(): CompleteFragment {

            val args = Bundle()
            val fragment = CompleteFragment()
            fragment.arguments = args
            return fragment
        }
    }


}