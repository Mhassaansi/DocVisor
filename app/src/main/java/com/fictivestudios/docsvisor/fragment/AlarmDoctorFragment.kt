package com.fictivestudios.docsvisor.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.adapter.AlarmDoctorAdapter
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.AlarmData
import com.fictivestudios.docsvisor.apiManager.response.CommonResponse
import com.fictivestudios.docsvisor.apiManager.response.GetAlarmResponse
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.constants.SELECT_USER
import com.fictivestudios.docsvisor.databinding.FragmentDoctorAlarmBinding
import com.fictivestudios.docsvisor.widget.TitleBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

open class AlarmDoctorFragment : BaseFragment(),OnItemClickListener {

    private var binding: FragmentDoctorAlarmBinding? = null

    private lateinit var alarmDoctorAdapter: AlarmDoctorAdapter
    private var arrayListSelector: ArrayList<AlarmData>? = null

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_doctor_alarm, container, false);
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
       /* if (alarmDoctorAdapter != null) {
            alarmDoctorAdapter.saveStates(outState)
        }*/
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
       /* if (alarmDoctorAdapter != null) {
            alarmDoctorAdapter.restoreStates(savedInstanceState)
        }*/
    }


    private fun bindData() {

        val mLayoutManager1: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding!!.rvAlarm.layoutManager = mLayoutManager1
        (binding!!.rvAlarm.itemAnimator as DefaultItemAnimator).supportsChangeAnimations =
            false


        getAlarmList()
    }


    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        if (sharedPreferenceManager?.getString(SELECT_USER).toString() == "Doctor") {
            titleBar?.showBackButtonWithAddButton(activity,"ALARM","Doctor")

        } else {
            titleBar?.showBackButtonWithAddButton(activity,"ALARM","Patient")
        }

    }

    override fun setListeners() {

    }


    private fun getAlarmList()
    {

        binding?.pbGetAlarm?.visibility = View.VISIBLE

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())

        GlobalScope.launch(Dispatchers.IO)
        {
            apiClient.getAlarm().enqueue(object: retrofit2.Callback<GetAlarmResponse> {
                override fun onResponse(
                    call: Call<GetAlarmResponse>,
                    response: Response<GetAlarmResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding?.pbGetAlarm?.visibility=View.GONE


                    })

                    Log.d("Response", response.message())



                    try {
                        Log.d("Response", response.body()?.message!!)

                        if (response.isSuccessful) {

                            if (response.body()?.status == 1) {


                                arrayListSelector = ArrayList()

                                arrayListSelector = response.body()?.data as ArrayList<AlarmData>

                                alarmDoctorAdapter = AlarmDoctorAdapter(
                                    requireContext(),
                                    arrayListSelector!!,
                                    this@AlarmDoctorFragment
                                )


                                binding!!.rvAlarm.adapter = alarmDoctorAdapter
                                alarmDoctorAdapter.notifyDataSetChanged()
                            }

/*                            arrayList = ArrayList()
                            arrayList = response.body()?.data

                            adapter = DoctorListAdapter(requireActivity(), arrayList!!, this@ListOfDoctorsFragment)
                            *//*arrayList?*//*
                            binding.rvDocList.adapter = adapter
                            adapter.notifyDataSetChanged()*/

                        }
                        else {
                            Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                        }
                    }
                    catch (e:Exception)
                    {
                        //Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<GetAlarmResponse>, t: Throwable)
                {

                    activity?.runOnUiThread(java.lang.Runnable {
                        binding?.pbGetAlarm?.visibility = View.GONE
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()


                    })
                }
            })

        }


    }



    companion object {
        fun newInstance(): AlarmDoctorFragment {
            val args = Bundle()
            val fragment = AlarmDoctorFragment()
            fragment.arguments = args
            return fragment
        }
    }



    override fun onItemClick(position: Int, `object`: Any?, view: View?, adapterName: String?) {


        deleteAlarm(arrayListSelector?.get(position)?.id.toString(),position)


    }
    private fun deleteAlarm(alarmId: String, position: Int)
    {

        binding?.pbGetAlarm?.visibility = View.VISIBLE

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("alaram_id", alarmId)
            .build()

        GlobalScope.launch(Dispatchers.IO)
        {
            apiClient.deleteAlarm(requestBody).enqueue(object: retrofit2.Callback<CommonResponse> {
                override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding?.pbGetAlarm?.visibility=View.GONE


                    })

                    Log.d("Response", response.message())

                    try {


                        if (response.isSuccessful) {

                            if (response.body()?.status == 1) {
                                activity?.runOnUiThread(java.lang.Runnable {

                                    arrayListSelector?.removeAt(position)
                                    alarmDoctorAdapter.notifyItemRemoved(position)
                                    alarmDoctorAdapter.notifyDataSetChanged()

                                    Toast.makeText(requireContext(), response?.body()?.message, Toast.LENGTH_SHORT).show()

                                })
                            }


                        }
                        else {
                            activity?.runOnUiThread(java.lang.Runnable {

                                Toast.makeText(requireContext(), response.message(), Toast.LENGTH_SHORT).show()

                            })
                        }
                    }
                    catch (e:Exception)
                    {

                        activity?.runOnUiThread(java.lang.Runnable {

                            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()

                        })
                    }
                }

                override fun onFailure(call: Call<CommonResponse>, t: Throwable)
                {

                    activity?.runOnUiThread(java.lang.Runnable {

                        binding?.pbGetAlarm?.visibility = View.GONE
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()


                    })
                }
            })

        }


    }

    override fun onResume() {
        super.onResume()
        getAlarmList()
    }

}