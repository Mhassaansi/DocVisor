package com.fictivestudios.docsvisor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.databinding.ItemDoctorPatientlistBinding
import com.fictivestudios.docsvisor.databinding.ItemSubsBinding
import com.fictivestudios.docsvisor.model.DummyAdapterModel

class DoctorPatientSubsAdapter(
    private val activity: Context?,
    private val arrData: List<DummyAdapterModel>,
    private val onItemClick: OnItemClickListener
) : RecyclerView.Adapter<DoctorPatientSubsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var layoutInflater: LayoutInflater? = null
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val commentAdapterBind = DataBindingUtil.inflate<ItemSubsBinding?>(
            layoutInflater!!,
            R.layout.item_subs,
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
           // bindTo(model)
            setListener(this, model)
        }
    }

    private fun setListener(holder: ViewHolder, model: DummyAdapterModel) {

        holder.run {

        }
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    /*class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val materialCardView4 = view.findViewById<MaterialCardView>(R.id.materialCardView4)
        var model: DummyAdapterModel? = null

        *//**
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.
         *//*
        fun bindTo(model: DummyAdapterModel?, context: Context) {
            this.model = model
            this.model?.let {

            }


        }
    }*/

    inner class ViewHolder(val dummyAdapterModel: ItemSubsBinding) :
        RecyclerView.ViewHolder(dummyAdapterModel.root) {
       /* fun bindTo(categoryViewModel: DummyAdapterModel?) {
            DummyAdapterModel.model = categoryViewModel
            DummyAdapterModel.executePendingBindings()
        }*/
    }

}