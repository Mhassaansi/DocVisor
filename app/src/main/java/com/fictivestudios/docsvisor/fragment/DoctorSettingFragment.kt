package com.fictivestudios.docsvisor.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.HomeActivity
import com.fictivestudios.docsvisor.databinding.FragmentDoctorSettingBinding
import com.fictivestudios.docsvisor.helper.PreferenceUtils
import com.fictivestudios.docsvisor.helper.USER_OBJECT
import com.fictivestudios.docsvisor.model.User
import com.fictivestudios.docsvisor.widget.TitleBar
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DoctorSettingFragment : BaseFragment() {

    lateinit var binding: FragmentDoctorSettingBinding
    lateinit var name: String
    private var userObj : User?=null
    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_doctor_setting, container, false);
        getUser()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    private fun getUser()
    {

        var task = GlobalScope.async(Dispatchers.IO) {
            var gson =  Gson()
            var json:String= PreferenceUtils.getString(USER_OBJECT)
            userObj=gson.fromJson(json, User::class.java)

        }
        GlobalScope.launch(Dispatchers.Main) {
            task.await()
            setUser()
        }

    }

    private fun setUser() {


        if (userObj!=null)
        {
            binding.tvname.setText(userObj?.name)
            binding.tvemail .setText(userObj?.email)
            binding.tvBio .setText(userObj?.bio)
            if (!userObj?.image.isNullOrEmpty())
            {
                Picasso.get().load(userObj?.image).into(binding.ivimage)

            }

        }


    }


    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showBackButton(activity,"SETTINGS")
    }

    override fun setListeners() {

        binding.subs.setOnClickListener {

            HomeActivity.navControllerHome.navigate(R.id.patientSubFragment2)
        }
    }

    companion object {

        fun newInstance(): DoctorSettingFragment {

            val args = Bundle()
            val fragment = DoctorSettingFragment()
            fragment.arguments = args
            return fragment
        }
    }


}