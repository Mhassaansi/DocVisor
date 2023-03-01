package com.fictivestudios.docsvisor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.apiManager.response.Report
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.databinding.ItemDoctorPatientProfileBinding
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.item_doctor_patient_profile_.view.*

class DoctorPatientTestAdapter(
    private val activity: Context?,
    private val arrData: List<Report>,
    private val onItemClick: OnItemClickListener
) : RecyclerView.Adapter<DoctorPatientTestAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
            .inflate(R.layout.item_doctor_patient_profile_, parent, false)
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
            bindTo(model,itemView)

            setListener(this, model)
        }
    }

    private fun setListener(holder: ViewHolder, model: Report) {

        holder.run {

        }
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val materialCardView4 = view.findViewById<MaterialCardView>(R.id.materialCardView4)
        var model: String? = null



        fun bindTo(model: Report, itemView: View) {

            itemView.dayName.setText(model.day_name)
            itemView.test.setText(model.result)

        }
    }



}