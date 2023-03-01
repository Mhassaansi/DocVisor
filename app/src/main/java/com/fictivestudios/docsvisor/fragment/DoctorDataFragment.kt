package com.fictivestudios.docsvisor.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager

import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.adapter.CustomAdapter
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.client.ApiInterface
import com.fictivestudios.docsvisor.apiManager.response.AppResponseModel
import com.fictivestudios.docsvisor.databinding.FragmentPatientDataBinding
import com.fictivestudios.docsvisor.helper.NetworkHelper
import com.fictivestudios.docsvisor.widget.TitleBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DoctorDataFragment : BaseFragment() {

    lateinit var binding: FragmentPatientDataBinding
    var DataList: ArrayList<String> = ArrayList<String>()
    private var adapter: CustomAdapter? = null
    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_patient_data, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRetrofitData()
    }

    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showBackButton(activity, "Data")
      //  titleBar?.showSidebar(getBaseActivity(), "Patient")
        //titleBar?.setTitle("Data")
    }

    override fun setListeners() {


    }




    fun getRetrofitData() {

        binding.pbData.visibility = View.VISIBLE

        val apiInterface: ApiInterface = ApiClient.RetrofitInstance.getApiServiceForWatchData(requireContext())

        if (!activity?.let { NetworkHelper.isNetworkConnected(it) }!!) {
            Toast.makeText(
                requireActivity(),
                "" + resources.getString(R.string.internet_connection),
                Toast.LENGTH_LONG
            ).show()
            return
        }
      /*  var appResponseModel: AppRequestModel = AppRequestModel()
        appResponseModel.data = DataList
        Log.e("ffdffd", "" + appResponseModel.data.toString())*/

        val call: Call<AppResponseModel<Any>> = apiInterface.getWatchData()
        call.enqueue(object : Callback<AppResponseModel<Any>> {
            override fun onResponse(
                call: Call<AppResponseModel<Any>>,
                response: Response<AppResponseModel<Any>>
            ) {
                binding.pbData.visibility = View.INVISIBLE
                try {
                    val appResponseModel: AppResponseModel<Any> = response.body()!!
                    val sucess: Int? = appResponseModel.status
                    val message: String = appResponseModel.message
                    if (sucess == 1) {

                        setupRecycleview(appResponseModel.data as ArrayList<AppResponseModel<Any>.Datum>)
                        Toast.makeText(requireActivity(), "" + message, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(requireActivity(), "" + message, Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {

                }
            }

            override fun onFailure(call: Call<AppResponseModel<Any>>, t: Throwable) {
                try {
                    binding.pbData.visibility = View.INVISIBLE
                    Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_LONG).show()
                    call.cancel()
                }
                catch (e:Exception){

                }
            }
        })
    }
    fun setupRecycleview(data: ArrayList<AppResponseModel<Any>.Datum>) {
        adapter = CustomAdapter(data, requireActivity())
        binding.listview.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.listview.setHasFixedSize(true)
        binding.listview.adapter = adapter
        adapter!!.notifyDataSetChanged()

    }
}