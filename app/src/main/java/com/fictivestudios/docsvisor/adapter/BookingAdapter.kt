package com.fictivestudios.docsvisor.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.apiManager.response.Data
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.fragment.BookAppointmentFragment.Companion.selectedTime
import kotlinx.android.synthetic.main.item_booking_list.view.*


class BookingAdapter(
    private val activity: Context?,
    private val arrData: ArrayList<Data>,
    private val onItemClick: OnItemClickListener
) : RecyclerView.Adapter<BookingAdapter.ViewHolder>() {

    private val binderHelper = ViewBinderHelper()
    var selectedPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
            .inflate(R.layout.item_booking_list, parent, false)
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

       /* holder.itemView.setOnClickListener {

        }*/

        // Bind your data here
        val model = arrData[position]
        with(holder) {



            bindTo(model, activity!!)

            // Checked selected radio button
            holder.itemView.cb_appoint.setChecked(
                position
                        === selectedPosition
            )

            if (position == selectedPosition)
            {
                selectedTime = model.time
            }
            // set listener on radio button

            // set listener on radio button
            holder.itemView.cb_appoint.setOnCheckedChangeListener(
                CompoundButton.OnCheckedChangeListener { compoundButton, b ->
                    // check condition
                    if (b) {
                        // When checked
                        // update selected position
                        selectedPosition = holder.adapterPosition
                        // Call listener
                        onItemClick.onItemClick(position,arrData[position]
                            ,compoundButton
                            ,BookingAdapter::class.java.simpleName)
                    }
                })







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
        val checkbox = view.findViewById<CheckBox>(R.id.cb_appoint)
        val tvTime = view.findViewById<AppCompatTextView>(R.id.tv_time)
        val tvAvailability = view.findViewById<AppCompatTextView>(R.id.tv_availability)
//        val layoutAlarm = view.findViewById<LinearLayoutCompat>(R.id.layoutAlarm)





        /**
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.
         */
        fun bindTo(model: Data, context: Context) {


           tvAvailability.text = model.status
            tvTime.text = model.time
        }
    }


}