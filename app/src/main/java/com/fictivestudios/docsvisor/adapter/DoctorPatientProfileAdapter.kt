package com.fictivestudios.docsvisor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.apiManager.response.PatientData
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import kotlinx.android.synthetic.main.item_doctor_profile_patientlist.view.*

class DoctorPatientProfileAdapter(
    private val activity: Context?,
    private val arrData: List<PatientData>,
    private val onItemClick: OnItemClickListener
) : RecyclerView.Adapter<DoctorPatientProfileAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
/*
        var layoutInflater: LayoutInflater? = null
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val commentAdapterBind = DataBindingUtil.inflate<ItemDoctorProfilePatientlistBinding?>(
            layoutInflater!!,
            R.layout.item_doctor_profile_patientlist,
            parent,
            false
        )
        return ViewHolder(commentAdapterBind!!)*/
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
            .inflate(R.layout.item_doctor_profile_patientlist, parent, false)
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
           bindTo(model,holder.itemView)
            setListener(this, model)
        }
    }

    private fun setListener(holder: ViewHolder, model: PatientData) {

        holder.run {


                onItemClick.onItemClick(
                    adapterPosition, model, holder.itemView,
                    DoctorPatientProfileAdapter::class.java.simpleName)

        }
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {





        fun bindTo(model: PatientData?, view: View) {

            view.tv_name.setText(model?.username)
            view.tv_email.setText(model?.email)



        }
    }


    }

