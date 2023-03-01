package com.fictivestudios.docsvisor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.apiManager.response.PendingAppointmentData
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_appointment_list.view.*
import kotlinx.android.synthetic.main.item_doctor_patientlist.view.*
import kotlinx.android.synthetic.main.item_request.view.*
import kotlinx.android.synthetic.main.item_request.view.imgLastActivityUser
import kotlinx.android.synthetic.main.item_request.view.tv_date
import kotlinx.android.synthetic.main.item_request.view.tv_name
import kotlinx.android.synthetic.main.item_request.view.tv_time


class RequestDoctorAdapter(
    private val activity: Context?,
    private val arrData: List<PendingAppointmentData>,
    private val onItemClick: OnItemClickListener
) : RecyclerView.Adapter<RequestDoctorAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {




        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
            .inflate(R.layout.item_request, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        /*val model = arrData[i]
        with(holder) {
            bindTo(model, activity!!)
            setListener(this, model)
        }*/

        val model = arrData[i]
        with(holder) {
            bindTo(model,activity!!,holder.itemView)

            setListener(this, model,i)
        }
    }

    private fun setListener(holder: ViewHolder, model: PendingAppointmentData, i: Int) {


            holder.itemView.btnaccept.setOnClickListener { view ->
                onItemClick.onItemClick(
                    i, model, view,
                    RequestDoctorAdapter::class.java.simpleName
                )
            }
        holder.itemView.btnReject.setOnClickListener { view ->
            onItemClick.onItemClick(
                i, model, view,
                RequestDoctorAdapter::class.java.simpleName
            )
        }

    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
/*        val materialCardView4 = view.findViewById<MaterialCardView>(R.id.materialCardView4)*/
        var model: String? = null
/*
        *
     * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
     * ViewHolder when Item is loaded.*/

        fun bindTo(model: PendingAppointmentData, context: Context, itemView: View) {


            itemView.tv_date.text =model.appointment_date
            itemView.tv_time.text = model.appointment_time
            itemView.tv_name.text =model.username


            if (!model?.image.isNullOrEmpty())
            {
                Picasso.get().load(model?.image).into(itemView.imgLastActivityUser)
            }

        }
    }


    }

