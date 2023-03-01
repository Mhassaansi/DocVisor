package com.fictivestudios.docsvisor.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.model.DummyAdapterModel
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper

class SubscriptionAdapter (
    private val activity: Context?,
    private val arrData: List<String>,
    private val onItemClick: OnItemClickListener
) : RecyclerView.Adapter<SubscriptionAdapter.ViewHolder>() {

    private val binderHelper = ViewBinderHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
            .inflate(R.layout.item_sub_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //     val model = arrData[position]
        //   binderHelper.setOpenOnlyOne(true)
//        if (arrData != null && 0 <= position && position < arrData.size) {
//
//            // Use ViewBindHelper to restore and save the open/close state of the SwipeRevealView
//            // put an unique string id as value, can be any string which uniquely define the data
//            binderHelper.bind(holder.swipeLayout, arrData.get(position).toString())

        // Bind your data here
        val model = arrData[position]
        with(holder) {
            bindTo(model, activity!!)

            // }
        }
    }

    fun saveStates(outState: Bundle?) {
        binderHelper.saveStates(outState)
    }

    fun restoreStates(inState: Bundle?) {
        binderHelper.restoreStates(inState)
    }



    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val swipeLayout = view.findViewById<SwipeRevealLayout>(R.id.swipeLayout)
        val layoutAlarm = view.findViewById<LinearLayoutCompat>(R.id.layoutAlarm)

        var model: DummyAdapterModel? = null


        /**
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.
         */
        fun bindTo(model: String?, context: Context) {
//            this.model = model
//            this.model?.let {
//
//            }


        }
    }


}