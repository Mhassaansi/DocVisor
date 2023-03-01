package com.fictivestudios.docsvisor.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.MainActivity
import com.fictivestudios.docsvisor.databinding.FragmentSubscriptionBinding
import com.fictivestudios.docsvisor.databinding.FragmentSuccessBinding
import com.fictivestudios.docsvisor.widget.TitleBar

class SuccessSubscriptionFragment : BaseFragment() {

    lateinit var binding: FragmentSubscriptionBinding
    lateinit var name: String

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_subscription, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.setTitle( "SUBSCRIPTION")
    }

    override fun setListeners() {

        binding.btnSuccess.setOnClickListener {
            MainActivity.navController.popBackStack(R.id.successSubscriptionFragment, true)
            MainActivity.navController.navigate(R.id.successSignupFragment)
        }
    }

    companion object {

        fun newInstance(): SuccessSubscriptionFragment {

            val args = Bundle()
            val fragment = SuccessSubscriptionFragment()
            fragment.arguments = args
            return fragment
        }
    }


}