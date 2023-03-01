package com.fictivestudios.docsvisor.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.model.DummyAdapterModel
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.fictivestudios.docsvisor.apiManager.response.GetNotificationData
import kotlinx.android.synthetic.main.item_doctor_notification.view.*
import java.text.SimpleDateFormat


class NotificationDoctorAdapter(
    private val activity: Context?,
    private val arrData: List<GetNotificationData>,
    private val onItemClick: OnItemClickListener
) : RecyclerView.Adapter<NotificationDoctorAdapter.ViewHolder>() {

    private val binderHelper = ViewBinderHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
            .inflate(R.layout.item_doctor_notification, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = arrData[position]
        binderHelper.setOpenOnlyOne(true)
        if (arrData != null && 0 <= position && position < arrData.size) {

            // Use ViewBindHelper to restore and save the open/close state of the SwipeRevealView
            // put an unique string id as value, can be any string which uniquely define the data
            binderHelper.bind(holder.swipeLayout, arrData.get(position).toString())

            // Bind your data here
            with(holder) {

                bindTo(model, holder.itemView)
                setListener(this, model)
            }

            holder.itemView.btn_delete.setOnClickListener {
                onItemClick.onItemClick(position,model,it,
                    NotificationDoctorAdapter::class.java.simpleName)
            }
        }
    }

    fun saveStates(outState: Bundle?) {
        binderHelper.saveStates(outState)
    }

    fun restoreStates(inState: Bundle?) {
        binderHelper.restoreStates(inState)
    }

    private fun setListener(holder: ViewHolder, model: GetNotificationData) {
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val swipeLayout = view.findViewById<SwipeRevealLayout>(R.id.swipeLayout)
        var model: DummyAdapterModel? = null

        /**
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.
         */
        fun bindTo(model: GetNotificationData, view: View) {


            view.tv_title.setText(model.title)
            view.tv_desc.setText(model.description)
            view.tv_date.setText(model.updated_at.getTime("yyyy-MM-dd'T'HH:ss:SSS","yyyy-MM-dd"))

        }
        fun String.getTime(input: String, output: String): String {
            val inputFormat = SimpleDateFormat(input)
            val outputFormat = SimpleDateFormat(output)
            val date = inputFormat.parse(this)
            return outputFormat.format(date ?: "")
        }
    }


}