package com.fictivestudios.docsvisor.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.databinding.DataBindingUtil
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.adapter.FitnessAdapter
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.*
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.constants.SELECT_USER
import com.fictivestudios.docsvisor.databinding.FragmentEditPatientProfileBinding
import com.fictivestudios.docsvisor.helper.*
import com.fictivestudios.docsvisor.widget.TitleBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class DoctorEditPatientProfile : BaseFragment(),OnItemClickListener{

    lateinit var binding: FragmentEditPatientProfileBinding
    lateinit var name: String
    private var arrayList: List<FitnessData>? = null
    private lateinit var adapter: FitnessAdapter

    var dayName:String?=null
    var fitnesId:String?=null
    var dayNote:String?=null
    var noteTextView:AppCompatEditText?=null

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
                R.layout.fragment_edit_patient_profile,
                container,
                false
            );
        arrayList = ArrayList()
        if (patientFitnessdata != null)
        {

            setData()
        }
        else if (arrayFitnessList!=null)
        {

            Log.d("arrayFitnessList", arrayFitnessList.toString())
            arrayList = arrayFitnessList
            adapter = FitnessAdapter(requireActivity(), arrayList!!, this@DoctorEditPatientProfile,true)
            binding.rvEditFitness.adapter = adapter
            adapter.notifyDataSetChanged()
        }


        return binding.root
    }

    private fun setData() {

        if (FITNESS_TYPE == FITNESS_TYPE_FITNESS)
        {
            arrayList = ArrayList()
            arrayList = patientFitnessdata?.fitness
            adapter = FitnessAdapter(requireActivity(), arrayList!!, this@DoctorEditPatientProfile,true)
            binding.rvEditFitness.adapter = adapter
            adapter.notifyDataSetChanged()


        }
        else if (FITNESS_TYPE == FITNESS_TYPE_DIET)
        {
            arrayList = ArrayList()
            arrayList = patientFitnessdata?.diet
            adapter = FitnessAdapter(requireActivity(), arrayList!!, this@DoctorEditPatientProfile,true)
            binding.rvEditFitness.adapter = adapter
            adapter.notifyDataSetChanged()
        }
        else if (FITNESS_TYPE == FITNESS_TYPE_MEDICS)
        {
            arrayList = ArrayList()
            arrayList = patientFitnessdata?.medics
            adapter = FitnessAdapter(requireActivity(), arrayList!!, this@DoctorEditPatientProfile,true)
            binding.rvEditFitness.adapter = adapter
            adapter.notifyDataSetChanged()
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        if (sharedPreferenceManager?.getString(SELECT_USER).toString() == "Doctor") {
            titleBar?.showBackButtonWithNotification(activity,"EDIT DIET","Doctor")

        } else {
            titleBar?.showBackButtonWithNotification(activity,"EDIT DIET","Patient")
        }
    }



/*    private fun getFitnessList()
    {
        binding.pbFitness.visibility=View.VISIBLE

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.getFitness(FITNESS_TYPE).enqueue(object: retrofit2.Callback<FitnessResponse> {
                override fun onResponse(
                    call: Call<FitnessResponse>,
                    response: Response<FitnessResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbFitness.visibility=View.GONE


                    })

                    Log.d("Response", response.message())

                    try {


                        if (response.isSuccessful) {

                            if (response.body()?.status==1)
                            {
                                Log.d("Response", response.body()!!.message)

                                Log.d("Response", response.body()!!.data.toString())

                                Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                                arrayList = ArrayList()
                                arrayList = response.body()?.data
                                adapter = FitnessAdapter(requireActivity(), arrayList!!, this@DoctorEditPatientProfile,true)
                                binding.rvEditFitness.adapter = adapter
                                adapter.notifyDataSetChanged()
                            }
                            else
                            {
                                Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                            }

                        }
                        else {
                            Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    catch (e:Exception)
                    {
                        //Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<FitnessResponse>, t: Throwable)
                {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                     activity?.runOnUiThread(java.lang.Runnable {
                         binding.pbFitness.visibility=View.GONE


                     })
                }
            })

        }


    }*/


    private fun updateFitness(day: String, type: String, note: String, fitnesId: String) {

        binding.pbFitness.visibility = View.VISIBLE

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)

            .addFormDataPart("day_name",day)
            .addFormDataPart("type", type)
            .addFormDataPart("note", note)
            .addFormDataPart("fitness_id", fitnesId)
            .build()


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.updateFitness(requestBody).enqueue(object: retrofit2.Callback<CommonResponse> {
                override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbFitness.visibility=View.GONE

                    })

                    Log.d("Response", response.body()?.message.toString())

                    try {


                        if (response.isSuccessful) {

                            if (response.body()?.status == 1)
                            {
                                Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()

                            }
                            else
                            {
                                Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()

                            }
                        }
                        else {
                            Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    catch (e:Exception)
                    {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CommonResponse>, t: Throwable)
                {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    /* activity?.runOnUiThread(java.lang.Runnable {
                         binding.pbChangePass.visibility=View.GONE
                         binding.btnSignUp.isEnabled=true
                         binding.btnSignUp.text="RESET"

                     })*/
                }
            })




        }
    }

    override fun setListeners() {

        binding.btnSave.setOnClickListener {


            dayNote = noteTextView?.text.toString()
           // dayNote

            Log.d("arrayFitnessList", dayName.toString()+ noteText.toString())

            if (!dayName.isNullOrEmpty() && !noteText.isNullOrEmpty() )
            {

                fitnesId?.let { it1 -> updateFitness(dayName!!, FITNESS_TYPE, noteText!!, it1) }
            }


        }
    }

    companion object {

        var noteText:String? = null
        fun newInstance(): DoctorEditPatientProfile {

            val args = Bundle()
            val fragment = DoctorEditPatientProfile()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onItemClick(position: Int, `object`: Any?, view: View?, adapterName: String?) {


    //    var o = `object` as FitnessData
        noteTextView =  (view as AppCompatEditText)
       // dayNote = (view as AppCompatEditText).text.toString()
        dayName =arrayList!![position].day_name
        fitnesId = arrayList!![position].id.toString()


    }

}