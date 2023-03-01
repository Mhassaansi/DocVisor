package com.fictivestudios.docsvisor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.databinding.ItemDoctorCertificateBinding
import com.fictivestudios.docsvisor.databinding.ItemDoctorListBinding
import com.fictivestudios.docsvisor.databinding.ItemDoctorPatientProfileBinding
import com.fictivestudios.docsvisor.model.DummyAdapterModel

class DoctorListDescAdapter(
    private val activity: Context?,
    private val arrData: List<String>,
    private val onItemClick: OnItemClickListener
) : RecyclerView.Adapter<DoctorListDescAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var layoutInflater: LayoutInflater? = null
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val commentAdapterBind = DataBindingUtil.inflate<ItemDoctorCertificateBinding?>(
            layoutInflater!!,
            R.layout.item_doctor_certificate,
            parent,
            false
        )
        return ViewHolder(commentAdapterBind!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        /*val model = arrData[i]
        with(holder) {
            bindTo(model, activity!!)
            setListener(this, model)
        }*/

        val model = arrData[i]
        with(holder) {
            bindTo(model)

            setListener(this, model)
        }
    }

    private fun setListener(holder: ViewHolder, model: String) {

        holder.run {
            dummyAdapterModel.layoutAlarm.setOnClickListener { view ->
                onItemClick.onItemClick(
                    adapterPosition, model, view,
                    DoctorPatientProfileAdapter::class.java.simpleName
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    /*class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val materialCardView4 = view.findViewById<MaterialCardView>(R.id.materialCardView4)
        var model: String? = null

        *//**
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.
         *//*
        fun bindTo(model: String?, context: Context) {
            this.model = model
            this.model?.let {

            }


        }
    }*/

    inner class ViewHolder(val dummyAdapterModel: ItemDoctorCertificateBinding) :
        RecyclerView.ViewHolder(dummyAdapterModel.root) {

        fun bindTo(categoryViewModel: String?) {
           // dummyAdapterModel.appCompatTextView.text = categoryViewModel!!.toString()
            /*DummyAdapterModel.model = categoryViewModel
            DummyAdapterModel.executePendingBindings()*/
        }
    }

}