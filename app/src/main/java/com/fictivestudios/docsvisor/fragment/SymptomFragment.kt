package com.fictivestudios.docsvisor.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.client.ApiInterface
import com.fictivestudios.docsvisor.apiManager.response.symptonchecker.*
import com.fictivestudios.docsvisor.callbacks.OnSpinnerOKPressedListener
import com.fictivestudios.docsvisor.databinding.FragmentPatientSymptomsBinding
import com.fictivestudios.docsvisor.helper.NetworkHelper
import com.fictivestudios.docsvisor.helper.PreferenceUtils
import com.fictivestudios.docsvisor.helper.USER_OBJECT
import com.fictivestudios.docsvisor.helper.X_RapidAPI_Key
import com.fictivestudios.docsvisor.model.SpinnerModel
import com.fictivestudios.docsvisor.model.User
import com.fictivestudios.docsvisor.widget.TitleBar
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SymptomFragment : BaseFragment() {

    lateinit var binding: FragmentPatientSymptomsBinding
    lateinit var name: String
    var spinnerSingleSelectDialogFragment: SpinnerDialogFragment? = null
    var bodypart1List: ArrayList<SpinnerModel> = ArrayList<SpinnerModel>()
    lateinit var bodyPart1Response: BodyPartResponse
    var bodypart2List: ArrayList<SpinnerModel> = ArrayList<SpinnerModel>()
    lateinit var bodyPart2Response: BodyPartResponse
    var bodypart3List: ArrayList<SpinnerModel> = ArrayList<SpinnerModel>()
    lateinit var bodyPart3Response: BodyPartResponse
    var bodypart4List: ArrayList<SpinnerModel> = ArrayList<SpinnerModel>()
    lateinit var bodyPart4Response: SubSymptonResponse
    var bodypart5List: ArrayList<SpinnerModel> = ArrayList<SpinnerModel>()
    lateinit var bodyPart5Response: BodyPartResponse
    var bodypart6List: ArrayList<SpinnerModel> = ArrayList<SpinnerModel>()
    lateinit var bodyPart6Response: DiagonisResponse
    var bodypart7List: ArrayList<SpinnerModel> = ArrayList<SpinnerModel>()
    lateinit var bodyPart7Response: BodyPartResponse
    var bodypart8List: ArrayList<SpinnerModel> = ArrayList<SpinnerModel>()
    lateinit var bodyPart8Response: SpecificationBasedOnDiagonisisResponse
    var bodypart9List: ArrayList<SpinnerModel> = ArrayList<SpinnerModel>()
    lateinit var bodyPart9Response: BodyPartResponse
    var symptonid = ""
    lateinit var issuesInfoResponse: IssuesInfoResponse
    lateinit var userObj: User
    lateinit var firstFourChars:String
    var lang:String="en-gb"

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_patient_symptoms, container, false);
        getBodyPartData()
        var gson = Gson()
        var json: String = PreferenceUtils.getString(USER_OBJECT)
         userObj = gson.fromJson(json, User::class.java)
        Log.e("value",""+userObj.gender)
        firstFourChars = userObj.date_of_birth.substring(0, 4);
        Log.e("value",""+firstFourChars)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showBackButton(activity, "SYMPTOM CHECKER")
    }

    override fun setListeners() {


        binding.bodypart1.setOnClickListener {
            if (bodypart1List.size != 0) {
                spinnerSingleSelectDialogFragment = SpinnerDialogFragment.newInstance(
                    "Please Select", bodypart1List, object : OnSpinnerOKPressedListener {
                        override fun onSingleSelection(data: SpinnerModel, selectedPosition: Int) {
                            binding.bodypart1txt.text = data.text
                            binding.bodypart2.visibility = View.GONE
                            binding.symptoms.visibility = View.GONE
                            binding.symptomsinbody.visibility = View.GONE
                            binding.healthissuse1.visibility = View.GONE
                            binding.healthissuse2.visibility = View.GONE
                            binding.diagonse.visibility = View.GONE
                            binding.proposedSympton.visibility = View.GONE
                            binding.specailizationbasedondiagonse.visibility = View.GONE
                            symptonid = bodyPart1Response.get(selectedPosition).ID.toString()
                            binding.bodypart2txt.setText("Body Part 2")
                            binding.syptoms.setText("Symptom")
                            binding.symptoninbody.setText("Symptom in body")
                            binding.healthissuse1text.setText("Health Issuse")
                            binding.healthissuse2text.setText("Health Issuse Info")
                            binding.diagonsetxt.setText("Diagonse")
                            binding.proposedSystyemtxt.setText("Proposed Symptons")
                            binding.specificationDiagonis.setText("Specailization based on diagonisis")
                            getSubBodyPartData(bodyPart1Response.get(selectedPosition).ID.toString())

                        }
                    },
                    0
                )
                spinnerSingleSelectDialogFragment!!.show(
                    requireActivity().supportFragmentManager,
                    "SpinnerDialogFragmentSingle"
                )
            } else {

            }

        }

        binding.bodypart2.setOnClickListener {
            if (bodypart2List.size != 0) {
                spinnerSingleSelectDialogFragment = SpinnerDialogFragment.newInstance(
                    "Please Select", bodypart2List, object : OnSpinnerOKPressedListener {
                        override fun onSingleSelection(data: SpinnerModel, selectedPosition: Int) {
                            binding.bodypart2txt.text = data.text
                            binding.bodypart2.visibility = View.VISIBLE
                            binding.symptoms.visibility = View.GONE
                            binding.symptomsinbody.visibility = View.GONE
                            binding.healthissuse1.visibility = View.GONE
                            binding.healthissuse2.visibility = View.GONE
                            binding.diagonse.visibility = View.GONE
                            binding.proposedSympton.visibility = View.GONE
                            binding.syptoms.setText("Symptom")
                            binding.symptoninbody.setText("Symptom in body")
                            binding.healthissuse1text.setText("Health Issuse")
                            binding.healthissuse2text.setText("Health Issuse Info")
                            binding.diagonsetxt.setText("Diagonse")
                            binding.proposedSystyemtxt.setText("Proposed Symptons")
                            binding.specificationDiagonis.setText("Specailization based on diagonisis")
                            binding.specailizationbasedondiagonse.visibility = View.GONE
                            getSymptonData()
                        }
                    },
                    0
                )
                spinnerSingleSelectDialogFragment!!.show(
                    requireActivity().supportFragmentManager,
                    "SpinnerDialogFragmentSingle"
                )
            }
        }

        binding.symptoms.setOnClickListener {
            if (bodypart3List.size != 0) {
                spinnerSingleSelectDialogFragment = SpinnerDialogFragment.newInstance(
                    "Please Select", bodypart3List, object : OnSpinnerOKPressedListener {
                        override fun onSingleSelection(data: SpinnerModel, selectedPosition: Int) {
                            binding.bodypart2.visibility = View.VISIBLE
                            binding.symptoms.visibility = View.VISIBLE
                            binding.symptomsinbody.visibility = View.GONE
                            binding.healthissuse1.visibility = View.GONE
                            binding.healthissuse2.visibility = View.GONE
                            binding.diagonse.visibility = View.GONE
                            binding.proposedSympton.visibility = View.GONE
                            binding.specailizationbasedondiagonse.visibility = View.GONE
                            binding.syptoms.text = data.text
                            binding.symptoninbody.setText("Symptom in body")
                            binding.healthissuse1text.setText("Health Issuse")
                            binding.healthissuse2text.setText("Health Issuse Info")
                            binding.diagonsetxt.setText("Diagonse")
                            binding.proposedSystyemtxt.setText("Proposed Symptons")
                            binding.specificationDiagonis.setText("Specailization based on diagonisis")
                            getSubSymbolData(bodyPart3Response.get(selectedPosition).ID.toString())
                        }
                    },
                    0
                )
                spinnerSingleSelectDialogFragment!!.show(
                    requireActivity().supportFragmentManager,
                    "SpinnerDialogFragmentSingle"
                )
            }
        }


        binding.symptomsinbody.setOnClickListener {
            if (bodypart4List.size != 0) {
                spinnerSingleSelectDialogFragment = SpinnerDialogFragment.newInstance(
                    "Please Select", bodypart4List, object : OnSpinnerOKPressedListener {
                        override fun onSingleSelection(data: SpinnerModel, selectedPosition: Int) {
                            binding.bodypart2.visibility = View.VISIBLE
                            binding.symptoms.visibility = View.VISIBLE
                            binding.symptomsinbody.visibility = View.VISIBLE
                            binding.healthissuse1.visibility = View.GONE
                            binding.healthissuse2.visibility = View.GONE
                            binding.diagonse.visibility = View.GONE
                            binding.proposedSympton.visibility = View.GONE
                            binding.specailizationbasedondiagonse.visibility = View.GONE
                            binding.symptoninbody.text = data.text
                            getHealthIssueData()
                            binding.healthissuse1text.setText("Health Issuse")
                            binding.healthissuse2text.setText("Health Issuse Info")
                            binding.diagonsetxt.setText("Diagonse")
                            binding.proposedSystyemtxt.setText("Proposed Symptons")
                            binding.specificationDiagonis.setText("Specailization based on diagonisis")
                        }
                    },
                    0
                )
                spinnerSingleSelectDialogFragment!!.show(
                    requireActivity().supportFragmentManager,
                    "SpinnerDialogFragmentSingle"
                )
            }
        }

        binding.healthissuse1.setOnClickListener {
            if (bodypart5List.size != 0) {
                spinnerSingleSelectDialogFragment = SpinnerDialogFragment.newInstance(
                    "Please Select", bodypart5List, object : OnSpinnerOKPressedListener {
                        override fun onSingleSelection(data: SpinnerModel, selectedPosition: Int) {
                            binding.bodypart2.visibility = View.VISIBLE
                            binding.symptoms.visibility = View.VISIBLE
                            if (bodypart4List.size == 0) {
                                binding.symptomsinbody.visibility = View.GONE

                            } else {
                                binding.symptomsinbody.visibility = View.VISIBLE
                            }
                            binding.healthissuse1.visibility = View.VISIBLE
                            binding.healthissuse2.visibility = View.GONE
                            binding.diagonse.visibility = View.GONE
                            binding.proposedSympton.visibility = View.GONE
                            binding.specailizationbasedondiagonse.visibility = View.GONE
                            binding.healthissuse1text.text = data.text
                            binding.healthissuse2text.setText("Health Issuse Info")
                            binding.diagonsetxt.setText("Diagonse")
                            binding.proposedSystyemtxt.setText("Proposed Symptons")
                            binding.specificationDiagonis.setText("Specailization based on diagonisis")
                            getHealthIssueInfoData(bodyPart5Response.get(selectedPosition).ID.toString())
                            getDiagonosisData(symptonid)
                        }
                    },
                    0
                )
                spinnerSingleSelectDialogFragment!!.show(
                    requireActivity().supportFragmentManager,
                    "SpinnerDialogFragmentSingle"
                )
            }
        }

        binding.healthissuse2.setOnClickListener {
            val cdd = CustomDialogClass(requireActivity(), issuesInfoResponse)
            cdd.show()
        }

        binding.diagonse.setOnClickListener {
            if (bodypart6List.size != 0) {
                spinnerSingleSelectDialogFragment = SpinnerDialogFragment.newInstance(
                    "Please Select", bodypart6List, object : OnSpinnerOKPressedListener {
                        override fun onSingleSelection(data: SpinnerModel, selectedPosition: Int) {
                            binding.bodypart2.visibility = View.VISIBLE
                            binding.symptoms.visibility = View.VISIBLE
                            if (bodypart4List.size == 0) {
                                binding.symptomsinbody.visibility = View.GONE

                            } else {
                                binding.symptomsinbody.visibility = View.VISIBLE
                            }

                            binding.healthissuse1.visibility = View.VISIBLE
                            binding.healthissuse2.visibility = View.VISIBLE
                            binding.diagonse.visibility = View.VISIBLE
                            binding.proposedSympton.visibility = View.GONE
                            binding.specailizationbasedondiagonse.visibility = View.GONE
                            binding.diagonsetxt.text = data.text
                            getProposedSymptonData()
                            binding.proposedSystyemtxt.setText("Proposed Symptons")
                            binding.specificationDiagonis.setText("Specailization based on diagonisis")

                        }
                    },
                    0
                )
                spinnerSingleSelectDialogFragment!!.show(
                    requireActivity().supportFragmentManager,
                    "SpinnerDialogFragmentSingle"
                )
            }
        }

        binding.proposedSympton.setOnClickListener {
            if (bodypart7List.size != 0) {
                spinnerSingleSelectDialogFragment = SpinnerDialogFragment.newInstance(
                    "Please Select", bodypart7List, object : OnSpinnerOKPressedListener {
                        override fun onSingleSelection(data: SpinnerModel, selectedPosition: Int) {
                            binding.bodypart2.visibility = View.VISIBLE
                            binding.symptoms.visibility = View.VISIBLE
                            if (bodypart4List.size == 0) {
                                binding.symptomsinbody.visibility = View.GONE

                            } else {
                                binding.symptomsinbody.visibility = View.VISIBLE
                            }
                         /*   if (bodypart6List.size == 0) {
                                binding.diagonse.visibility = View.GONE

                            } else {
                                binding.diagonse.visibility = View.VISIBLE
                            }*/
                            binding.healthissuse1.visibility = View.VISIBLE
                            binding.healthissuse2.visibility = View.VISIBLE
                            binding.proposedSympton.visibility = View.VISIBLE
                            binding.specailizationbasedondiagonse.visibility = View.GONE
                            binding.proposedSystyemtxt.text = data.text
                            getSpecificationBasedonDiagonisData()
                            binding.specificationDiagonis.setText("Specailization based on diagonisis")

                        }
                    },
                    0
                )
                spinnerSingleSelectDialogFragment!!.show(
                    requireActivity().supportFragmentManager,
                    "SpinnerDialogFragmentSingle"
                )
            }
        }

        binding.specailizationbasedondiagonse.setOnClickListener {
            if (bodypart8List.size != 0) {
                spinnerSingleSelectDialogFragment = SpinnerDialogFragment.newInstance(
                    "Please Select", bodypart8List, object : OnSpinnerOKPressedListener {
                        override fun onSingleSelection(data: SpinnerModel, selectedPosition: Int) {
                            binding.bodypart2.visibility = View.VISIBLE
                            binding.symptoms.visibility = View.VISIBLE
                            if (bodypart4List.size == 0) {
                                binding.symptomsinbody.visibility = View.GONE

                            } else {
                                binding.symptomsinbody.visibility = View.VISIBLE
                            }
                            binding.healthissuse1.visibility = View.VISIBLE
                            binding.healthissuse2.visibility = View.VISIBLE
                         /*   if (bodypart6List.size == 0) {
                                binding.diagonse.visibility = View.GONE

                            } else {
                                binding.diagonse.visibility = View.VISIBLE
                            }
                            if (bodypart7List.size == 0) {
                                binding.proposedSympton.visibility = View.GONE

                            } else {
                                binding.proposedSympton.visibility = View.VISIBLE

                            }*/
                            binding.specailizationbasedondiagonse.visibility = View.VISIBLE
                            binding.specificationDiagonis.text = data.text
                            getSpecificationData()
                        }
                    },
                    0
                )
                spinnerSingleSelectDialogFragment!!.show(
                    requireActivity().supportFragmentManager,
                    "SpinnerDialogFragmentSingle"
                )
            }
        }


    binding.linearLayoutCompat2.setOnClickListener{
        bodypart1List.clear()
        bodypart2List.clear()
        bodypart3List.clear()
        bodypart4List.clear()
        bodypart5List.clear()
        bodypart6List.clear()
        bodypart7List.clear()
        bodypart8List.clear()
        bodypart9List.clear()
        symptonid=""
        binding.bodypart2.visibility = View.GONE
        binding.symptoms.visibility = View.GONE
        binding.symptomsinbody.visibility = View.GONE
        binding.healthissuse1.visibility = View.GONE
        binding.healthissuse2.visibility = View.GONE
        binding.diagonse.visibility = View.GONE
        binding.proposedSympton.visibility = View.GONE
        binding.specailizationbasedondiagonse.visibility = View.GONE
        binding.bodypart1txt.setText("Body Part 1")
        binding.bodypart2txt.setText("Body Part 2")
        binding.syptoms.setText("Symptom")
        binding.symptoninbody.setText("Symptom in body")
        binding.healthissuse1text.setText("Health Issuse")
        binding.healthissuse2text.setText("Health Issuse Info")
        binding.diagonsetxt.setText("Diagonse")
        binding.proposedSystyemtxt.setText("Proposed Symptons")
        binding.specificationDiagonis.setText("Specailization based on diagonisis")


    }
    }

    companion object {

        fun newInstance(): SymptomFragment {

            val args = Bundle()
            val fragment = SymptomFragment()
            fragment.arguments = args
            return fragment
        }
    }

    fun getBodyPartData() {

        binding.progressbar.visibility = View.VISIBLE

        val apiInterface: ApiInterface =
            ApiClient.RetrofitInstance.getApiServiceForSymptonChecker(requireContext())

        if (!activity?.let { NetworkHelper.isNetworkConnected(it) }!!) {
            Toast.makeText(
                requireActivity(),
                "" + resources.getString(R.string.internet_connection),
                Toast.LENGTH_LONG
            ).show()
            binding.progressbar.visibility = View.GONE

            return
        }
        GlobalScope.launch(Dispatchers.IO)
        {


            val call: Call<BodyPartResponse> = apiInterface.bodyPart(lang, X_RapidAPI_Key)
            call.enqueue(object : Callback<BodyPartResponse> {
                override fun onResponse(
                    call: Call<BodyPartResponse>,
                    response: Response<BodyPartResponse>
                ) {
                    try {
                        binding.progressbar.visibility = View.GONE
                        bodypart1List.clear()
                        bodyPart1Response = response.body()!!
                        Log.e("bodyPartResponse", "" + bodyPart1Response.get(0).Name)
                        for (i in bodyPart1Response.indices) {
                            bodypart1List.add(SpinnerModel(bodyPart1Response.get(i).Name))
                        }


                    } catch (e: Exception) {
                        e.printStackTrace()
                        binding.progressbar.visibility = View.GONE

                    }
                }

                override fun onFailure(call: Call<BodyPartResponse>, t: Throwable) {
                    try {
                        binding.progressbar.visibility = View.GONE
                        Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_LONG).show()
                        call.cancel()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
        }
    }


    fun getSubBodyPartData(bodypartId: String) {
        binding.progressbar.visibility = View.VISIBLE


        val apiInterface: ApiInterface =
            ApiClient.RetrofitInstance.getApiServiceForSymptonChecker(requireContext())

        if (!activity?.let { NetworkHelper.isNetworkConnected(it) }!!) {
            Toast.makeText(
                requireActivity(),
                "" + resources.getString(R.string.internet_connection),
                Toast.LENGTH_LONG
            ).show()
            binding.progressbar.visibility = View.GONE

            return
        }
        GlobalScope.launch(Dispatchers.IO)
        {

            val call: Call<BodyPartResponse> =
                apiInterface.subBodyPart(bodypartId, bodypartId, lang, X_RapidAPI_Key)
            call.enqueue(object : Callback<BodyPartResponse> {
                override fun onResponse(
                    call: Call<BodyPartResponse>,
                    response: Response<BodyPartResponse>
                ) {
                    try {
                        binding.progressbar.visibility = View.GONE

                        bodypart2List.clear()
                        bodyPart2Response = response.body()!!
                        Log.e("bodyPartResponse", "" + bodyPart2Response.get(0).Name)
                        for (i in bodyPart2Response.indices) {
                            bodypart2List.add(SpinnerModel(bodyPart2Response.get(i).Name))
                        }
                        binding.bodypart2.visibility = View.VISIBLE
                        binding.symptoms.visibility = View.GONE
                        binding.symptomsinbody.visibility = View.GONE
                        binding.healthissuse1.visibility = View.GONE
                        binding.healthissuse2.visibility = View.GONE
                        binding.diagonse.visibility = View.GONE
                        binding.proposedSympton.visibility = View.GONE
                        binding.specailizationbasedondiagonse.visibility = View.GONE

                    } catch (e: Exception) {
                        e.printStackTrace()
                        binding.progressbar.visibility = View.GONE

                    }
                }

                override fun onFailure(call: Call<BodyPartResponse>, t: Throwable) {
                    try {
                        binding.progressbar.visibility = View.GONE

                        Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_LONG).show()
                        call.cancel()
                    } catch (e: Exception) {
                        Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_LONG).show()
                        e.printStackTrace()
                    }
                }
            })
        }
    }


    fun getSymptonData() {


        val apiInterface: ApiInterface =
            ApiClient.RetrofitInstance.getApiServiceForSymptonChecker(requireContext())
        binding.progressbar.visibility = View.VISIBLE

        if (!activity?.let { NetworkHelper.isNetworkConnected(it) }!!) {
            Toast.makeText(
                requireActivity(),
                "" + resources.getString(R.string.internet_connection),
                Toast.LENGTH_LONG
            ).show()
            binding.progressbar.visibility = View.GONE

            return
        }

        GlobalScope.launch(Dispatchers.IO)
        {

            val call: Call<BodyPartResponse> = apiInterface.sypmtons(lang, X_RapidAPI_Key)
            call.enqueue(object : Callback<BodyPartResponse> {
                override fun onResponse(
                    call: Call<BodyPartResponse>,
                    response: Response<BodyPartResponse>
                ) {
                    try {
                        bodypart3List.clear()
                        bodyPart3Response = response.body()!!
                        Log.e("bodyPartResponse", "" + bodyPart3Response.get(0).Name)
                        for (i in bodyPart3Response.indices) {
                            bodypart3List.add(SpinnerModel(bodyPart3Response.get(i).Name))
                        }
                        binding.bodypart2.visibility = View.VISIBLE
                        binding.symptoms.visibility = View.VISIBLE
                        binding.symptomsinbody.visibility = View.GONE
                        binding.healthissuse1.visibility = View.GONE
                        binding.healthissuse2.visibility = View.GONE
                        binding.diagonse.visibility = View.GONE
                        binding.proposedSympton.visibility = View.GONE
                        binding.specailizationbasedondiagonse.visibility = View.GONE
                        binding.progressbar.visibility = View.GONE


                    } catch (e: Exception) {
                        e.printStackTrace()
                        binding.progressbar.visibility = View.GONE

                    }
                }

                override fun onFailure(call: Call<BodyPartResponse>, t: Throwable) {
                    try {
                        binding.progressbar.visibility = View.GONE
                        Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_LONG).show()
                        call.cancel()
                    } catch (e: Exception) {
                        Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_LONG).show()
                        e.printStackTrace()
                    }
                }
            })
        }
    }

    fun getSubSymbolData(bodypartId: String) {


        val apiInterface: ApiInterface =
            ApiClient.RetrofitInstance.getApiServiceForSymptonChecker(requireContext())
        binding.progressbar.visibility = View.VISIBLE

        if (!activity?.let { NetworkHelper.isNetworkConnected(it) }!!) {
            Toast.makeText(
                requireActivity(),
                "" + resources.getString(R.string.internet_connection),
                Toast.LENGTH_LONG
            ).show()
            binding.progressbar.visibility = View.GONE

            return
        }
        GlobalScope.launch(Dispatchers.IO)
        {
            var params:String=""
           if(userObj.gender.equals("male")){
               params="man"
               }
               else{
               params="woman"
           }
            val call: Call<SubSymptonResponse> = apiInterface.subSympton(
                bodypartId,
                params,
                params,
                bodypartId,
                lang,
                X_RapidAPI_Key
            )
            call.enqueue(object : Callback<SubSymptonResponse> {
                override fun onResponse(
                    call: Call<SubSymptonResponse>,
                    response: Response<SubSymptonResponse>
                ) {
                    try {
                        bodypart4List.clear()
                        bodyPart4Response = response.body()!!
                        if (bodyPart4Response.size == 0) {
                            getHealthIssueData()
                            binding.bodypart2.visibility = View.VISIBLE
                            binding.symptoms.visibility = View.VISIBLE
                            binding.symptomsinbody.visibility = View.GONE
                            binding.healthissuse1.visibility = View.GONE
                            binding.healthissuse2.visibility = View.GONE
                            binding.diagonse.visibility = View.GONE
                            binding.proposedSympton.visibility = View.GONE
                            binding.specailizationbasedondiagonse.visibility = View.GONE
                        } else {
                            Log.e("bodyPartResponse", "" + bodyPart4Response.get(0).Name)
                            for (i in bodyPart4Response.indices) {
                                bodypart4List.add(SpinnerModel(bodyPart4Response.get(i).Name))
                            }
                            binding.bodypart2.visibility = View.VISIBLE
                            binding.symptoms.visibility = View.VISIBLE
                            binding.symptomsinbody.visibility = View.VISIBLE
                            binding.healthissuse1.visibility = View.GONE
                            binding.healthissuse2.visibility = View.GONE
                            binding.diagonse.visibility = View.GONE
                            binding.proposedSympton.visibility = View.GONE
                            binding.specailizationbasedondiagonse.visibility = View.GONE
                        }
                        binding.progressbar.visibility = View.GONE

                    } catch (e: Exception) {
                        e.printStackTrace()
                        binding.progressbar.visibility = View.GONE

                    }
                }

                override fun onFailure(call: Call<SubSymptonResponse>, t: Throwable) {
                    try {
                        Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_LONG).show()
                        call.cancel()
                        binding.progressbar.visibility = View.GONE

                    } catch (e: Exception) {
                        Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_LONG).show()
                        e.printStackTrace()
                    }
                }
            })
        }
    }

    fun getHealthIssueData() {

        binding.progressbar.visibility = View.VISIBLE

        val apiInterface: ApiInterface =
            ApiClient.RetrofitInstance.getApiServiceForSymptonChecker(requireContext())

        if (!activity?.let { NetworkHelper.isNetworkConnected(it) }!!) {
            Toast.makeText(
                requireActivity(),
                "" + resources.getString(R.string.internet_connection),
                Toast.LENGTH_LONG
            ).show()
            binding.progressbar.visibility = View.GONE

            return
        }
        GlobalScope.launch(Dispatchers.IO)
        {
            val call: Call<BodyPartResponse> = apiInterface.healthissues1(lang, X_RapidAPI_Key)
            call.enqueue(object : Callback<BodyPartResponse> {
                override fun onResponse(
                    call: Call<BodyPartResponse>,
                    response: Response<BodyPartResponse>
                ) {
                    try {
                        binding.progressbar.visibility = View.GONE

                        bodypart5List.clear()
                        bodyPart5Response = response.body()!!
                        Log.e("bodyPartResponse", "" + bodyPart5Response.get(0).Name)
                        for (i in bodyPart5Response.indices) {
                            bodypart5List.add(SpinnerModel(bodyPart5Response.get(i).Name))
                        }
                        binding.bodypart2.visibility = View.VISIBLE
                        binding.symptoms.visibility = View.VISIBLE
                        if (bodypart4List.size == 0) {
                            binding.symptomsinbody.visibility = View.GONE

                        } else {
                            binding.symptomsinbody.visibility = View.VISIBLE
                        }
                        binding.healthissuse1.visibility = View.VISIBLE
                        binding.healthissuse2.visibility = View.GONE
                        binding.diagonse.visibility = View.GONE
                        binding.proposedSympton.visibility = View.GONE
                        binding.specailizationbasedondiagonse.visibility = View.GONE


                    } catch (e: Exception) {
                        e.printStackTrace()
                        binding.progressbar.visibility = View.GONE

                    }
                }

                override fun onFailure(call: Call<BodyPartResponse>, t: Throwable) {
                    try {
                        binding.progressbar.visibility = View.GONE
                        Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_LONG).show()
                        call.cancel()
                    } catch (e: Exception) {
                        Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_LONG).show()
                        e.printStackTrace()
                    }
                }
            })
        }
    }

    fun getHealthIssueInfoData(issuesid: String) {


        val apiInterface: ApiInterface =
            ApiClient.RetrofitInstance.getApiServiceForSymptonChecker(requireContext())
        binding.progressbar.visibility = View.VISIBLE

        if (!activity?.let { NetworkHelper.isNetworkConnected(it) }!!) {
            Toast.makeText(
                requireActivity(),
                "" + resources.getString(R.string.internet_connection),
                Toast.LENGTH_LONG
            ).show()
            binding.progressbar.visibility = View.GONE

            return
        }
        GlobalScope.launch(Dispatchers.IO)
        {
            val call: Call<IssuesInfoResponse> =
                apiInterface.healthissues2(issuesid, lang, X_RapidAPI_Key)
            call.enqueue(object : Callback<IssuesInfoResponse> {
                override fun onResponse(
                    call: Call<IssuesInfoResponse>,
                    response: Response<IssuesInfoResponse>
                ) {
                    try {
                         issuesInfoResponse = response.body()!!
                        binding.progressbar.visibility = View.GONE

                        binding.bodypart2.visibility = View.VISIBLE
                        binding.symptoms.visibility = View.VISIBLE
                        if (bodypart4List.size == 0) {
                            binding.symptomsinbody.visibility = View.GONE

                        } else {
                            binding.symptomsinbody.visibility = View.VISIBLE
                        }
                        binding.healthissuse1.visibility = View.VISIBLE
                        binding.healthissuse2.visibility = View.VISIBLE
                        binding.diagonse.visibility = View.GONE
                        binding.proposedSympton.visibility = View.GONE
                        binding.specailizationbasedondiagonse.visibility = View.GONE



                    } catch (e: Exception) {
                        e.printStackTrace()
                        binding.progressbar.visibility = View.GONE

                    }
                }

                override fun onFailure(call: Call<IssuesInfoResponse>, t: Throwable) {
                    try {
                        binding.progressbar.visibility = View.GONE
                        Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_LONG).show()
                        call.cancel()
                    } catch (e: Exception) {
                        Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_LONG).show()
                        e.printStackTrace()
                    }
                }
            })
        }
    }

    fun getDiagonosisData(symptonid: String) {

        binding.progressbar.visibility = View.VISIBLE
        val apiInterface: ApiInterface =
            ApiClient.RetrofitInstance.getApiServiceForSymptonChecker(requireContext())

        if (!activity?.let { NetworkHelper.isNetworkConnected(it) }!!) {
            Toast.makeText(
                requireActivity(),
                "" + resources.getString(R.string.internet_connection),
                Toast.LENGTH_LONG
            ).show()
            binding.progressbar.visibility = View.GONE

            return
        }
        GlobalScope.launch(Dispatchers.IO)
        {


            val call: Call<DiagonisResponse> =
                apiInterface.diagonsis(
                    lang,
                    userObj.gender,
                    firstFourChars,
                    "[" + symptonid + "]",
                    X_RapidAPI_Key
                )
            call.enqueue(object : Callback<DiagonisResponse> {
                override fun onResponse(
                    call: Call<DiagonisResponse>,
                    response: Response<DiagonisResponse>
                ) {
                    try {
                        bodypart6List.clear()
                        bodyPart6Response = response.body()!!
                        if (bodyPart6Response.size != 0) {

                            Log.e("bodyPartResponse", "" + bodyPart6Response.get(0).Issue.IcdName)
                            for (j in bodyPart6Response.indices) {
                                bodypart6List.add(SpinnerModel(bodyPart6Response.get(j).Issue.IcdName))
                            }
                            binding.progressbar.visibility = View.GONE

                            binding.bodypart2.visibility = View.VISIBLE
                            binding.symptoms.visibility = View.VISIBLE
                            if (bodypart4List.size == 0) {
                                binding.symptomsinbody.visibility = View.GONE

                            } else {
                                binding.symptomsinbody.visibility = View.VISIBLE
                            }
                            binding.healthissuse1.visibility = View.VISIBLE
                            binding.healthissuse2.visibility = View.VISIBLE
                            binding.diagonse.visibility = View.VISIBLE
                            binding.proposedSympton.visibility = View.GONE
                            binding.specailizationbasedondiagonse.visibility = View.GONE
                        } else {
                            binding.progressbar.visibility = View.GONE
                            getProposedSymptonData()
                            binding.bodypart2.visibility = View.VISIBLE
                            binding.symptoms.visibility = View.VISIBLE
                            if (bodypart4List.size == 0) {
                                binding.symptomsinbody.visibility = View.GONE

                            } else {
                                binding.symptomsinbody.visibility = View.VISIBLE
                            }
                            binding.healthissuse1.visibility = View.VISIBLE
                            binding.healthissuse2.visibility = View.VISIBLE
                            binding.diagonse.visibility = View.GONE
                            binding.proposedSympton.visibility = View.GONE
                            binding.specailizationbasedondiagonse.visibility = View.GONE
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        binding.progressbar.visibility = View.GONE

                    }
                }

                override fun onFailure(call: Call<DiagonisResponse>, t: Throwable) {
                    try {
                        binding.progressbar.visibility = View.GONE
                        Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_LONG).show()
                        call.cancel()
                    } catch (e: Exception) {
                        Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_LONG).show()
                        e.printStackTrace()
                    }
                }
            })
        }
    }

    fun getProposedSymptonData() {
        binding.progressbar.visibility = View.VISIBLE

        val apiInterface: ApiInterface =
            ApiClient.RetrofitInstance.getApiServiceForSymptonChecker(requireContext())

        if (!activity?.let { NetworkHelper.isNetworkConnected(it) }!!) {
            Toast.makeText(
                requireActivity(),
                "" + resources.getString(R.string.internet_connection),
                Toast.LENGTH_LONG
            ).show()
            binding.progressbar.visibility = View.GONE

            return
        }
        GlobalScope.launch(Dispatchers.IO)
        {


            val call: Call<BodyPartResponse> =
                apiInterface.proposed(
                    lang,
                    userObj.gender,
                    firstFourChars,
                    "[" + symptonid + "]",
                    X_RapidAPI_Key
                )
            call.enqueue(object : Callback<BodyPartResponse> {
                override fun onResponse(
                    call: Call<BodyPartResponse>,
                    response: Response<BodyPartResponse>
                ) {
                    try {
                        bodypart7List.clear()
                        bodyPart7Response = response.body()!!

                        if (bodyPart7Response.size == 0) {


                            binding.progressbar.visibility = View.GONE

                            binding.bodypart2.visibility = View.VISIBLE
                            binding.symptoms.visibility = View.VISIBLE
                            if (bodypart4List.size == 0) {
                                binding.symptomsinbody.visibility = View.GONE

                            } else {
                                binding.symptomsinbody.visibility = View.VISIBLE
                            }
                            binding.healthissuse1.visibility = View.VISIBLE
                            binding.healthissuse2.visibility = View.VISIBLE
                            if (bodypart6List.size == 0) {
                                binding.diagonse.visibility = View.GONE

                            } else {
                                binding.diagonse.visibility = View.VISIBLE
                            }
                            getSpecificationBasedonDiagonisData()
                            binding.proposedSympton.visibility = View.GONE
                            binding.specailizationbasedondiagonse.visibility = View.GONE
                        } else {
                            Log.e("bodyPartResponse", "1" + bodyPart7Response.get(0).Name)
                            for (j in bodyPart7Response.indices) {
                                bodypart7List.add(SpinnerModel(bodyPart7Response.get(j).Name))
                            }
                            binding.progressbar.visibility = View.GONE

                            binding.bodypart2.visibility = View.VISIBLE
                            binding.symptoms.visibility = View.VISIBLE
                            if (bodypart4List.size == 0) {
                                binding.symptomsinbody.visibility = View.GONE

                            } else {
                                binding.symptomsinbody.visibility = View.VISIBLE
                            }
                            binding.healthissuse1.visibility = View.VISIBLE
                            binding.healthissuse2.visibility = View.VISIBLE
                            if (bodypart6List.size == 0) {
                                binding.diagonse.visibility = View.GONE

                            } else {
                                binding.diagonse.visibility = View.VISIBLE
                            }
                            binding.proposedSympton.visibility = View.VISIBLE
                            binding.specailizationbasedondiagonse.visibility = View.GONE


                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        binding.progressbar.visibility = View.GONE

                    }
                }

                override fun onFailure(call: Call<BodyPartResponse>, t: Throwable) {
                    try {
                        binding.progressbar.visibility = View.GONE
                        Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_LONG).show()
                        call.cancel()
                    } catch (e: Exception) {
                        Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_LONG).show()
                        e.printStackTrace()
                    }
                }
            })
        }
    }


    fun getSpecificationBasedonDiagonisData() {
        binding.progressbar.visibility = View.VISIBLE

        val apiInterface: ApiInterface =
            ApiClient.RetrofitInstance.getApiServiceForSymptonChecker(requireContext())

        if (!activity?.let { NetworkHelper.isNetworkConnected(it) }!!) {
            Toast.makeText(
                requireActivity(),
                "" + resources.getString(R.string.internet_connection),
                Toast.LENGTH_LONG
            ).show()
            binding.progressbar.visibility = View.GONE

            return
        }
        GlobalScope.launch(Dispatchers.IO)
        {


            val call: Call<SpecificationBasedOnDiagonisisResponse> =
                apiInterface.diagonisBasedOnSpecification(
                    lang,
                    userObj.gender,
                    firstFourChars,
                    "[" + symptonid + "]",
                    X_RapidAPI_Key
                )
            call.enqueue(object : Callback<SpecificationBasedOnDiagonisisResponse> {
                override fun onResponse(
                    call: Call<SpecificationBasedOnDiagonisisResponse>,
                    response: Response<SpecificationBasedOnDiagonisisResponse>
                ) {
                    try {
                        bodypart8List.clear()
                        bodyPart8Response = response.body()!!
                        if(bodyPart8Response.size!=0){
                            Log.e("bodyPartResponse", "" + bodyPart8Response.get(0).Name)
                            for (j in bodyPart8Response.indices) {
                                bodypart8List.add(SpinnerModel(bodyPart8Response.get(j).Name))
                            }
                            binding.progressbar.visibility = View.GONE

                            binding.bodypart2.visibility = View.VISIBLE
                            binding.symptoms.visibility = View.VISIBLE
                            if (bodypart4List.size == 0) {
                                binding.symptomsinbody.visibility = View.GONE

                            } else {
                                binding.symptomsinbody.visibility = View.VISIBLE
                            }
                            binding.healthissuse1.visibility = View.VISIBLE
                            binding.healthissuse2.visibility = View.VISIBLE
                            if (bodypart6List.size == 0) {
                                binding.diagonse.visibility = View.GONE

                            } else {
                                binding.diagonse.visibility = View.VISIBLE
                            }
                            if (bodypart7List.size == 0) {
                                binding.proposedSympton.visibility = View.GONE

                            } else {
                                binding.proposedSympton.visibility = View.VISIBLE

                            }
                            binding.proposedSympton.visibility = View.VISIBLE
                            binding.specailizationbasedondiagonse.visibility = View.VISIBLE
                        }
                        else{
                            binding.progressbar.visibility = View.GONE

                            binding.bodypart2.visibility = View.VISIBLE
                            binding.symptoms.visibility = View.VISIBLE
                            if (bodypart4List.size == 0) {
                                binding.symptomsinbody.visibility = View.GONE

                            } else {
                                binding.symptomsinbody.visibility = View.VISIBLE
                            }
                            binding.healthissuse1.visibility = View.VISIBLE
                            binding.healthissuse2.visibility = View.VISIBLE
                            if (bodypart6List.size == 0) {
                                binding.diagonse.visibility = View.GONE

                            } else {
                                binding.diagonse.visibility = View.VISIBLE
                            }
                            if (bodypart7List.size == 0) {
                                binding.proposedSympton.visibility = View.GONE

                            } else {
                                binding.proposedSympton.visibility = View.VISIBLE

                            }
                            binding.proposedSympton.visibility = View.VISIBLE
                            binding.specailizationbasedondiagonse.visibility = View.GONE
                            getSpecificationData()
                        }



                    } catch (e: Exception) {
                        e.printStackTrace()
                        binding.progressbar.visibility = View.GONE

                    }
                }

                override fun onFailure(
                    call: Call<SpecificationBasedOnDiagonisisResponse>,
                    t: Throwable
                ) {
                    try {
                        binding.progressbar.visibility = View.GONE
                        Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_LONG).show()
                        call.cancel()
                    } catch (e: Exception) {
                        Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_LONG).show()
                        e.printStackTrace()
                    }
                }
            })
        }
    }

    fun getSpecificationData() {

        binding.progressbar.visibility = View.VISIBLE

        val apiInterface: ApiInterface =
            ApiClient.RetrofitInstance.getApiServiceForSymptonChecker(requireContext())

        if (!activity?.let { NetworkHelper.isNetworkConnected(it) }!!) {
            Toast.makeText(
                requireActivity(),
                "" + resources.getString(R.string.internet_connection),
                Toast.LENGTH_LONG
            ).show()
            binding.progressbar.visibility = View.GONE

            return
        }
        GlobalScope.launch(Dispatchers.IO)
        {


            val call: Call<BodyPartResponse> = apiInterface.speciification(lang, X_RapidAPI_Key)
            call.enqueue(object : Callback<BodyPartResponse> {
                override fun onResponse(
                    call: Call<BodyPartResponse>,
                    response: Response<BodyPartResponse>
                ) {
                    try {
                        binding.progressbar.visibility = View.GONE
                        bodypart9List.clear()
                        bodyPart9Response = response.body()!!
                        Log.e("bodyPartResponse", "" + bodyPart9Response.get(0).Name)
                        for (i in bodyPart9Response.indices) {
                            bodypart9List.add(SpinnerModel(bodyPart9Response.get(i).Name))
                        }


                    } catch (e: Exception) {
                        e.printStackTrace()
                        binding.progressbar.visibility = View.GONE

                    }
                }

                override fun onFailure(call: Call<BodyPartResponse>, t: Throwable) {
                    try {
                        binding.progressbar.visibility = View.GONE
                        Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_LONG).show()
                        call.cancel()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
        }
    }


    inner class CustomDialogClass(var c: Context, issuesInfoResponse: IssuesInfoResponse) :
        Dialog(c, R.style.Theme_Dialog) {
        var missuesInfoResponse = issuesInfoResponse
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.health_details_dialog)
            var description = findViewById<View>(R.id.description) as TextView?
            var descriptionshort = findViewById<View>(R.id.descriptionshort) as TextView?
            var name = findViewById<View>(R.id.name) as TextView?
            var profName = findViewById<View>(R.id.profName) as TextView?
            var synoyms = findViewById<View>(R.id.synoyms) as TextView?
            var medical_condition = findViewById<View>(R.id.medical_condition) as TextView?
            var possiblesympton = findViewById<View>(R.id.possiblesympton) as TextView?
            var treatmentdescription = findViewById<View>(R.id.treatmentdescription) as TextView?

            description!!.text=""+missuesInfoResponse.Description
            descriptionshort!!.text=""+missuesInfoResponse.DescriptionShort
            name!!.text=""+missuesInfoResponse.Name
            profName!!.text=""+missuesInfoResponse.ProfName
            synoyms!!.text=""+missuesInfoResponse.Synonyms
            medical_condition!!.text=""+missuesInfoResponse.MedicalCondition
            possiblesympton!!.text=""+missuesInfoResponse.PossibleSymptoms
            treatmentdescription!!.text=""+missuesInfoResponse.TreatmentDescription

        }
    }

}