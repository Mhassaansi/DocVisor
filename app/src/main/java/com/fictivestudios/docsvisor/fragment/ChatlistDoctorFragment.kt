package com.fictivestudios.docsvisor.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.HomeActivity
import com.fictivestudios.docsvisor.activities.HomeActivityPateint
import com.fictivestudios.docsvisor.adapter.ChatListDoctorAdapter
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.ChatListData
import com.fictivestudios.docsvisor.apiManager.response.ChatListResponse
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.constants.SELECT_USER
import com.fictivestudios.docsvisor.databinding.FragmentDoctorChatBinding
import com.fictivestudios.docsvisor.widget.TitleBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

open class ChatlistDoctorFragment : BaseFragment(),OnItemClickListener{

    private var binding: FragmentDoctorChatBinding? = null

    private  var chatListAdapter: ChatListDoctorAdapter?=null
    private var arrayListChat: List<ChatListData>? = null

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_doctor_chat, container, false);
        getChatList()
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arrayListChat = ArrayList()

        bindData()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (chatListAdapter != null) {
            chatListAdapter?.saveStates(outState)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (chatListAdapter != null) {
            chatListAdapter?.restoreStates(savedInstanceState)
        }
    }


    private fun bindData() {


        val mLayoutManager1: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding!!.rvChatList!!.layoutManager = mLayoutManager1
        (binding!!.rvChatList.itemAnimator as DefaultItemAnimator).supportsChangeAnimations =
            false


    }


    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        if (sharedPreferenceManager?.getString(SELECT_USER).toString() == "Doctor") {
            titleBar?.showBackButtonWithNotification(activity,"CHAT","Doctor")

        } else {
            titleBar?.showBackButtonWithNotification(activity,"CHAT","Patient")
        }

    }

    override fun setListeners() {

    }



    private fun getChatList()
    {

        binding?.pbGetchat?.visibility=View.VISIBLE

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.getChatList().enqueue(object: retrofit2.Callback<ChatListResponse> {
                override fun onResponse(
                    call: Call<ChatListResponse>,
                    response: Response<ChatListResponse>
                )
                {



                    activity?.runOnUiThread(java.lang.Runnable {
                        binding?.pbGetchat?.visibility=View.GONE


                    })

                    Log.d("Response", response.message())

                    try {


                        if (response.isSuccessful) {
                            Log.d("Response", response.body()!!.status.toString())
                            if (response.body()?.status==1)
                            {
                                Log.d("Response", response.body()!!.status.toString())

                                Log.d("Response", response.body()!!.data.toString())

                                Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()


                                    arrayListChat = response.body()?.data

                                if (!arrayListChat.isNullOrEmpty())
                                {
                                    chatListAdapter = ChatListDoctorAdapter(requireContext(), arrayListChat!!, this@ChatlistDoctorFragment)
                                    binding!!.rvChatList!!.adapter = chatListAdapter
                                    chatListAdapter?.notifyDataSetChanged()

                                }
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
                        activity?.runOnUiThread(java.lang.Runnable {
                            binding?.pbGetchat?.visibility=View.GONE
                            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()

                        })
                    }
                }

                override fun onFailure(call: Call<ChatListResponse>, t: Throwable)
                {

                    activity?.runOnUiThread(java.lang.Runnable {
                        binding?.pbGetchat?.visibility=View.GONE
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()

                    })
                }
            })

        }


    }


    companion object {
        fun newInstance(): ChatlistDoctorFragment {
            val args = Bundle()
            val fragment = ChatlistDoctorFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onItemClick(position: Int, `object`: Any?, view: View?, adapterName: String?) {

        if (sharedPreferenceManager?.getString(SELECT_USER).toString() == "Doctor") {

            val bundle = bundleOf("receiverUserId" to arrayListChat?.get(position)?.userID.toString()
                ,"username" to arrayListChat?.get(position)?.name.toString())

            HomeActivity.navControllerHome.navigate(R.id.messengerFragment, bundle)
        } else {
            val bundle = bundleOf("receiverUserId" to arrayListChat?.get(position)?.userID.toString()
                ,"username" to arrayListChat?.get(position)?.name.toString())

            HomeActivityPateint.navControllerPatient?.navigate(R.id.messengerFragment2, bundle)


        }
    }



}