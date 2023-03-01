package com.fictivestudios.docsvisor.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.adapter.DoctorListThirdPartyAdapter
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.doctorthird.DoctorThridPartApiModel
import com.fictivestudios.docsvisor.apiManager.response.doctorthird.Result
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.databinding.DoctorThirdPartListFragmentBinding
import com.fictivestudios.docsvisor.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_doctor_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class DoctorThirdPartyFragment : BaseFragment() , OnItemClickListener {

    lateinit var doctorThirdBinding: DoctorThirdPartListFragmentBinding
    private var arrayList: ArrayList<Result>? = null
    private lateinit var adapter: DoctorListThirdPartyAdapter

    override fun getDrawerLockMode(): Int {

        return 0
    }

    override fun setTitlebar(titleBar: TitleBar?) {

        titleBar?.visibility = View.VISIBLE
        titleBar?.showSidebar(getBaseActivity(), "Patient")
        titleBar?.setTitle("DOCTOR LISTS")
    }

    override fun setListeners() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        doctorThirdBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.doctor_third_part_list_fragment,
                container,
                false)

        return doctorThirdBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mLayoutManager2: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvDocList.layoutManager = mLayoutManager2
        (rvDocList.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false



        getDoctersThirdPartyList()
    }


    private fun getDoctersThirdPartyList()
    {

        doctorThirdBinding.pbDoctor.visibility=View.VISIBLE

        val apiClient = ApiClient.RetrofitInstance.getDoctorThirdApi(requireContext())


        lifecycleScope.launch(Dispatchers.IO)
        {

            apiClient.doctorThirdAPI().enqueue(object: retrofit2.Callback<DoctorThridPartApiModel> {
                override fun onResponse(
                    call: Call<DoctorThridPartApiModel>,
                    response: Response<DoctorThridPartApiModel>
                ) {
                    activity?.runOnUiThread(java.lang.Runnable {
                        doctorThirdBinding.pbDoctor.visibility = View.GONE

                    })

                    Log.d("scheduleResponse", response.message())

                    try {


                        if (response.isSuccessful) {
                            arrayList = ArrayList()
                            var response : DoctorThridPartApiModel = response.body()!!
                            arrayList = response.results

                            adapter = DoctorListThirdPartyAdapter(requireActivity(),
                                arrayList!!, this@DoctorThirdPartyFragment)

                            doctorThirdBinding.rvDocList.adapter = adapter
                            doctorThirdBinding.rvDocList.adapter?.notifyDataSetChanged()



                            doctorThirdBinding.txtHeadingHeartRate.addTextChangedListener(object :TextWatcher{
                                override fun beforeTextChanged(
                                    p0: CharSequence?, p1: Int, p2: Int, p3: Int
                                ) {

                                }

                                override fun onTextChanged(
                                    p0: CharSequence?, p1: Int, p2: Int, p3: Int
                                ) {

                                    adapter.filter.filter(p0)
                                   // adapter.notifyDataSetChanged()

                                }

                                override fun afterTextChanged(p0: Editable?) {

                                }

                            })


                        } else {
                            Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {

                    }
                }

                override fun onFailure(call: Call<DoctorThridPartApiModel>, t: Throwable) {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    activity?.runOnUiThread(java.lang.Runnable {
                        doctorThirdBinding.pbDoctor.visibility=View.GONE

                    })
                }

            })
        }
    }
                override fun onItemClick(
                    position: Int,
                    `object`: Any?,
                    view: View?,
                    adapterName: String?
                ) {}


            }