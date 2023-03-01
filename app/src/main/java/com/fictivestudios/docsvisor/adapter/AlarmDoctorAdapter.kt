package com.fictivestudios.docsvisor.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat

import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.apiManager.response.AlarmData


class AlarmDoctorAdapter(
    private val activity: Context?,
    private val arrData: List<AlarmData>,
    private val onItemClick: OnItemClickListener
) : RecyclerView.Adapter<AlarmDoctorAdapter.ViewHolder>() {

    private val binderHelper = ViewBinderHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
            .inflate(R.layout.item_doctor_alarm, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = arrData[position]
        binderHelper.setOpenOnlyOne(true)
        if (arrData != null && 0 <= position && position < arrData.size) {

            // Use ViewBindHelper to restore and save the open/close state of the SwipeRevealView
            // put an unique string id as value, can be any string which uniquely define the data
           // binderHelper.bind(holder.swipeLayout, arrData.get(position).toString())

            // Bind your data here
            with(holder) {

                bindTo(model, activity!!)
                setListener(this, model)
            }
        }

    }

    fun saveStates(outState: Bundle?) {
        binderHelper.saveStates(outState)
    }

    fun restoreStates(inState: Bundle?) {
        binderHelper.restoreStates(inState)
    }

    private fun setListener(holder: ViewHolder, model: AlarmData) {
        holder.run {

/*            layoutAlarm.setOnClickListener { view ->
                onItemClick.onItemClick(
                    adapterPosition, model, view,
                    "click"
                )
            }*/

            btnDelete.setOnClickListener {
                onItemClick.onItemClick(
                    adapterPosition, model, it,
                    AlarmDoctorAdapter::class.java.name
                )
            }

        }
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val swipeLayout = view.findViewById<SwipeRevealLayout>(R.id.swipeLayout)
        val layoutAlarm = view.findViewById<LinearLayoutCompat>(R.id.layoutAlarm)

        val btnDelete = view.findViewById<ImageView>(R.id.btn_delete)

        val tv_name = view.findViewById<AppCompatTextView>(R.id.tv_patient)
        val tv_desc = view.findViewById<AppCompatTextView>(R.id.tv_desc)
        val tv_date = view.findViewById<AppCompatTextView>(R.id.tv_date_d)
        val tv_time = view.findViewById<AppCompatTextView>(R.id.tv_time_d)

        /**
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.
         */
        fun bindTo(model: AlarmData, context: Context) {

            //tv_name.text = model.
            tv_date.text = model.alaram_date
            tv_time.text = model.alaram_time.toString()
            tv_desc.text = model.note



        }
    }


}