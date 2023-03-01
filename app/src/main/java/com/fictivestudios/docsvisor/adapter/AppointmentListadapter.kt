package com.fictivestudios.docsvisor.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.model.DummyAdapterModel
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.fictivestudios.docsvisor.apiManager.response.AppointmentData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_appointment_list.view.*
import kotlinx.android.synthetic.main.item_doctor_patientlist.view.*

class AppointmentListadapter(
    private val activity: Context?,
    private val arrData: List<AppointmentData>,
    private val onItemClick: OnItemClickListener
) : RecyclerView.Adapter<AppointmentListadapter.ViewHolder>() {

    private val binderHelper = ViewBinderHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
            .inflate(R.layout.item_appointment_list, parent, false)
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


        holder.itemView.setOnClickListener {

            onItemClick.onItemClick(
                position, arrData[position], it,
                AppointmentListadapter::class.java.simpleName
            )
        }

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
        fun bindTo(model: AppointmentData, context: Context) {
//            this.model = model
//            this.model?.let {
//
//            }
            val name = itemView.findViewById<TextView>(R.id.tv_name)
            val profession = itemView.findViewById<TextView>(R.id.tv_profession)
            val profession2 = itemView.findViewById<TextView>(R.id.tv_profession_2)
            val tv_time = itemView.findViewById<TextView>(R.id.tv_time)
            val tv_date = itemView.findViewById<TextView>(R.id.tv_date)


            name.setText(model.username.toString())
          //  profession.setText(model.profession.toString())
            tv_time.setText(model.appointment_time)
            tv_date.setText(model.appointment_date)
            if (!model?.image.isNullOrEmpty())
            {
                Picasso.get().load(model?.image).into(itemView.imgLastActivityUser)
            }



            profession.visibility = View.INVISIBLE
            profession2.visibility = View.INVISIBLE

        }
    }


}