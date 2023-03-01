package com.fictivestudios.docsvisor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.apiManager.response.OffDaysData
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.databinding.OffdaysAdapterbind
import kotlinx.android.synthetic.main.item_off_days.view.*

class OffdaysAdapter(
    private val activity: Context?,
    private val arrData: List<OffDaysData>,
    private val onItemClick: OnItemClickListener
) : RecyclerView.Adapter<OffdaysAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var layoutInflater: LayoutInflater? = null
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val commentAdapterBind = DataBindingUtil.inflate<OffdaysAdapterbind?>(
            layoutInflater!!,
            R.layout.item_off_days,
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
            bindTo(model,holder.itemView)

            setListener(this, model)
        }
    }

    private fun setListener(holder: ViewHolder, model: OffDaysData) {

        /*holder.run {
            dummyAdapterModel.layoutAlarm.setOnClickListener { view ->
                onItemClick.onItemClick(
                    adapterPosition, model, view,"item"
                )
            }
        }*/

        holder.itemView.iv_delete.setOnClickListener {
            onItemClick.onItemClick(holder.adapterPosition,model,holder.itemView,OffdaysAdapter::class.java.simpleName)
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

    inner class ViewHolder(val dummyAdapterModel: OffdaysAdapterbind) :
        RecyclerView.ViewHolder(dummyAdapterModel.root) {

        fun bindTo(model: OffDaysData, itemView: View) {
            itemView.tv_to_date.text=model.to_date
            itemView.tv_from_date.text=model.from_date
        }
    }

}