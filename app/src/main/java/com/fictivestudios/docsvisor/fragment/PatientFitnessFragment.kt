package com.fictivestudios.docsvisor.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.HomeActivityPateint
import com.fictivestudios.docsvisor.adapter.ExpandableListAdapter
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.FitnessData
import com.fictivestudios.docsvisor.apiManager.response.FitnessResponse
import com.fictivestudios.docsvisor.constants.DOCTOR_BOTTOMNAV

import com.fictivestudios.docsvisor.databinding.FragmentPatientFitnessBinding
import com.fictivestudios.docsvisor.helper.*
import com.fictivestudios.docsvisor.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_patient_fitness.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class PatientFitnessFragment : BaseFragment() {

    lateinit var binding: FragmentPatientFitnessBinding
    lateinit var name: String
    var listAdapter: ExpandableListAdapter? = null
    var listDataHeader: List<String>? = null
    var listDataChild: HashMap<String, List<String>>? = null



    private var arrayList: List<FitnessData>? = null


    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_patient_fitness, container, false);


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferenceManager?.putValue(
                DOCTOR_BOTTOMNAV,
                "FitnessPatient"
        )
        HomeActivityPateint.home.patientFitness()
        HomeActivityPateint.home.showBottomBar()
        binding.txtDiet.background.setTint(resources.getColor(R.color.colorPrimary))
        binding.txtDiet.setTextColor(Color.parseColor("#ffffff"))
        binding.txtFitness.background.setTint(resources.getColor(R.color.white))
        binding.txtFitness.setTextColor(Color.parseColor("#6f64fe"))
        binding.txtMedics.background.setTint(resources.getColor(R.color.white))
        binding.txtMedics.setTextColor(Color.parseColor("#6f64fe"))

        binding.txtDiet.setOnClickListener {

            FITNESS_TYPE = FITNESS_TYPE_DIET

            binding.txtDiet.background.setTint(resources.getColor(R.color.colorPrimary))
            binding.txtDiet.setTextColor(Color.parseColor("#ffffff"))
            binding.txtFitness.background.setTint(resources.getColor(R.color.white))
            binding.txtFitness.setTextColor(Color.parseColor("#6f64fe"))
            binding.txtMedics.background.setTint(resources.getColor(R.color.white))
            binding.txtMedics.setTextColor(Color.parseColor("#6f64fe"))

            getFitnessList(FITNESS_TYPE)
        }

        binding.txtFitness.setOnClickListener {

            FITNESS_TYPE = FITNESS_TYPE_FITNESS

            binding.txtDiet.background.setTint(resources.getColor(R.color.white))
            binding.txtDiet.setTextColor(Color.parseColor("#6f64fe"))
            binding.txtFitness.background.setTint(resources.getColor(R.color.colorPrimary))
            binding.txtFitness.setTextColor(Color.parseColor("#ffffff"))
            binding.txtMedics.background.setTint(resources.getColor(R.color.white))
            binding.txtMedics.setTextColor(Color.parseColor("#6f64fe"))

            getFitnessList(FITNESS_TYPE)
        }

        binding.txtMedics.setOnClickListener {

            FITNESS_TYPE = FITNESS_TYPE_MEDICS

            binding.txtDiet.background.setTint(resources.getColor(R.color.white))
            binding.txtDiet.setTextColor(Color.parseColor("#6f64fe"))
            binding.txtFitness.background.setTint(resources.getColor(R.color.white))
            binding.txtFitness.setTextColor(Color.parseColor("#6f64fe"))
            binding.txtMedics.background.setTint(resources.getColor(R.color.colorPrimary))
            binding.txtMedics.setTextColor(Color.parseColor("#ffffff"))

            getFitnessList(FITNESS_TYPE)
        }


        // preparing list data

        // preparing list data

        getFitnessList(FITNESS_TYPE)
//        prepareListData(arrayList)


        // setting list adapter

        // setting list adapter


        // Listview Group click listener

        // Listview Group click listener
        lvExp!!.setOnGroupClickListener(object : ExpandableListView.OnGroupClickListener {
            override fun onGroupClick(parent: ExpandableListView?, v: View?,
                                      groupPosition: Int, id: Long): Boolean {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false
            }
        })

        // Listview Group expanded listener

        // Listview Group expanded listener
        lvExp!!.setOnGroupExpandListener(object : ExpandableListView.OnGroupExpandListener {
            override fun onGroupExpand(groupPosition: Int) {
                /*Toast.makeText(context, listDataHeader!![groupPosition] + " Expanded",
                        Toast.LENGTH_SHORT).show()*/
            }
        })

        // Listview Group collasped listener

        // Listview Group collasped listener
        lvExp!!.setOnGroupCollapseListener(object : ExpandableListView.OnGroupCollapseListener {
            override fun onGroupCollapse(groupPosition: Int) {
                /*Toast.makeText(context, listDataHeader!![groupPosition] + " Collapsed",
                        Toast.LENGTH_SHORT).show()*/
            }
        })

        // Listview on child click listener

        // Listview on child click listener
        lvExp!!.setOnChildClickListener(object : ExpandableListView.OnChildClickListener {
            override fun onChildClick(parent: ExpandableListView?, v: View?,
                             groupPosition: Int, childPosition: Int, id: Long): Boolean {
                // TODO Auto-generated method stub
                Toast.makeText(
                        context,
                        listDataHeader!![groupPosition] + " : "
                                + listDataChild!![listDataHeader!![groupPosition]]!![childPosition], Toast.LENGTH_SHORT)
                        .show()
                return false
            }
        })
    }

    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
       // titleBar?.showBackButton(activity, "FITNESS")
        titleBar?.showSidebar(getBaseActivity(), "Patient")
        titleBar?.setTitle("FITNESS")
    }

    override fun setListeners() {

        binding.imgEdit.setOnClickListener {
            HomeActivityPateint.navControllerPatient!!.navigate(R.id.doctorEditPatientProfile2)
        }
    }

    companion object {

        fun newInstance(): PatientFitnessFragment {

            val args = Bundle()
            val fragment = PatientFitnessFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun prepareListData(arrayList: List<FitnessData>?) {
        listDataHeader = ArrayList()
        listDataChild = HashMap()

        // Adding child data

        for (item in arrayList!!.indices)
        {
            (listDataHeader as ArrayList<String>).add(arrayList[item].day_name)


            val comingSoon: MutableList<String> = ArrayList()
            comingSoon.add(arrayList[item].note)

            listDataChild!![(listDataHeader as ArrayList<String>).get(item)] = comingSoon

        }
        listAdapter = ExpandableListAdapter(context, listDataHeader, listDataChild)
        lvExp!!.setAdapter(listAdapter)




/*        (listDataHeader as ArrayList<String>).add("Monday")
        (listDataHeader as ArrayList<String>).add("Tuesday")
        (listDataHeader as ArrayList<String>).add("Wednesday")
        (listDataHeader as ArrayList<String>).add("Thursday")
        (listDataHeader as ArrayList<String>).add("Friday")
        (listDataHeader as ArrayList<String>).add("Saturday")
        (listDataHeader as ArrayList<String>).add("Sunday")*/

      /*  val comingSoon: MutableList<String> = ArrayList()
        comingSoon.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")

        listDataChild!![(listDataHeader as ArrayList<String>).get(0)] = comingSoon // Header, Child data
        listDataChild!![(listDataHeader as ArrayList<String>).get(1)] = comingSoon
        listDataChild!![(listDataHeader as ArrayList<String>).get(2)] = comingSoon
        listDataChild!![(listDataHeader as ArrayList<String>).get(3)] = comingSoon
        listDataChild!![(listDataHeader as ArrayList<String>).get(4)] = comingSoon
        listDataChild!![(listDataHeader as ArrayList<String>).get(5)] = comingSoon
        listDataChild!![(listDataHeader as ArrayList<String>).get(6)] = comingSoon*/
    }

    private fun getFitnessList(type: String)
    {


        binding.pbPFitness.visibility = View.VISIBLE
        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.getFitness(type).enqueue(object: retrofit2.Callback<FitnessResponse> {
                override fun onResponse(
                    call: Call<FitnessResponse>,
                    response: Response<FitnessResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        binding.pbPFitness.visibility=View.GONE

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
                                prepareListData(arrayList)
                                arrayFitnessList = response.body()?.data as ArrayList<FitnessData>
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
                    /* activity?.runOnUiThread(java.lang.Runnable {
                         binding.pbChangePass.visibility=View.GONE
                         binding.btnSignUp.isEnabled=true
                         binding.btnSignUp.text="RESET"

                     })*/
                }
            })

        }


    }

}