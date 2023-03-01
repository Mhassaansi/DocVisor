package com.fictivestudios.docsvisor.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.model.DummyAdapterModel
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.fictivestudios.docsvisor.apiManager.response.ChatListData
import kotlinx.android.synthetic.main.item_doctor_chat.view.*


class ChatListDoctorAdapter(
    private val activity: Context?,
    private val arrData: List<ChatListData>,
    private val onItemClick: OnItemClickListener
) : RecyclerView.Adapter<ChatListDoctorAdapter.ViewHolder>() {

    private val binderHelper = ViewBinderHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
            .inflate(R.layout.item_doctor_chat, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = arrData[position]
        binderHelper.setOpenOnlyOne(true)
        if (arrData != null && 0 <= position && position < arrData.size) {

            // Use ViewBindHelper to restore and save the open/close state of the SwipeRevealView
            // put an unique string id as value, can be any string which uniquely define the data
            holder.bindTo(model, activity!!)
            setListener(holder, model)

            binderHelper.bind(holder.swipeLayout, arrData.get(position).toString())

            // Bind your data here



        }
    }

    fun saveStates(outState: Bundle?) {
        binderHelper.saveStates(outState)
    }

    fun restoreStates(inState: Bundle?) {
        binderHelper.restoreStates(inState)
    }

    private fun setListener(holder: ViewHolder, model: ChatListData) {


            holder.itemView.layoutChat.setOnClickListener { view ->
                onItemClick.onItemClick(
                    holder.adapterPosition, model, view,
                    ChatListDoctorAdapter::class.java.simpleName
                )
            }

    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val swipeLayout = view.findViewById<SwipeRevealLayout>(R.id.swipeLayout)
        val layoutChat = view.findViewById<LinearLayoutCompat>(R.id.layoutChat)

        val tvName = view.findViewById<AppCompatTextView>(R.id.tv_name)
        val tvMsg = view.findViewById<AppCompatTextView>(R.id.tv_message)

        /**
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.
         */
        fun bindTo(model: ChatListData, context: Context) {


            tvMsg.text=model.msg
            tvName.text=model.name



        }
    }


}