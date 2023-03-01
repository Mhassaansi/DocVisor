package com.fictivestudios.docsvisor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.apiManager.response.PatientData
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_appointment_list.view.*
import kotlinx.android.synthetic.main.item_doctor_patientlist.view.*
import kotlinx.android.synthetic.main.item_doctor_patientlist.view.tv_name

class DoctorPatientListAdapter(
    private val activity: Context?,
    private val arrData: List<PatientData>,
    private val onItemClick: OnItemClickListener
) : RecyclerView.Adapter<DoctorPatientListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

    /*    var layoutInflater: LayoutInflater? = null
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val commentAdapterBind = DataBindingUtil.inflate<ItemDoctorPatientlistBinding?>(
            layoutInflater!!,
            R.layout.item_doctor_patientlist,
            parent,
            false
        )
        return ViewHolder(commentAdapterBind!!)*/

        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
            .inflate(R.layout.item_doctor_patientlist, parent, false)
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
            bindTo(holder.itemView,model,activity!!)
            setListener(this, model)
        }
    }

    private fun setListener(holder: ViewHolder, model: PatientData) {

        holder.run {
            holder.itemView.menu.setOnClickListener { view ->
                onItemClick.onItemClick(
                    adapterPosition, model, view,
                    DoctorPatientListAdapter::class.java.simpleName
                )
            }
            holder.itemView.materialCardView.setOnClickListener { view ->
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

/*
        *
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.*/

        fun bindTo(itemView: View, model: PatientData?, context: Context) {


            itemView.tv_name.text = model?.username
            itemView.tv_email.text = model?.email

            if (!model?.image.isNullOrEmpty())
            {
                Picasso.get().load(model?.image).into(itemView.iv_image)
            }



        }
    }

/*    inner class ViewHolder(val dummyAdapterModel: ItemDoctorPatientlistBinding) :
        RecyclerView.ViewHolder(dummyAdapterModel.root) {
       *//* fun bindTo(categoryViewModel: DummyAdapterModel?) {
            DummyAdapterModel.model = categoryViewModel
            DummyAdapterModel.executePendingBindings()
        }*//*
    }*/

}