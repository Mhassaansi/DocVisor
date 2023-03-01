package com.fictivestudios.docsvisor.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.apiManager.response.ScheduleData
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener

class DaysAdapter(private val activity: Context?,
                  private val arrData: List<ScheduleData>,
                  private val onItemClick: OnItemClickListener
) : RecyclerView.Adapter<DaysAdapter.ViewHolder>() {

    private val binderHelper = ViewBinderHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
            .inflate(R.layout.item_days, parent, false)
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

    private fun setListener(holder: ViewHolder, model: ScheduleData) {
        holder.run {

            holder.itemView .setOnClickListener { view ->
                onItemClick.onItemClick(
                    adapterPosition, model, view,
                    DoctorPatientListAdapter::class.java.simpleName
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvday = view.findViewById<AppCompatTextView>(R.id.tv_days)
        val tv_time = view.findViewById<AppCompatTextView>(R.id.tv_time_days)

        /**
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.
         */
        fun bindTo(model: ScheduleData, context: Context) {

            //tv_name.text = model.
            tvday.text = model.day
            tv_time.text = model.from_time+" "+model.to_time




        }
    }

}