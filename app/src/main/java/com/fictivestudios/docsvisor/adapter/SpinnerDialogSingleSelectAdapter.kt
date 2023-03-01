package com.fictivestudios.docsvisor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.callbacks.OnSpinnerItemClickListener
import com.fictivestudios.docsvisor.model.SpinnerModel

class SpinnerDialogSingleSelectAdapter(
    private val context: Context,
    var arrData: ArrayList<SpinnerModel>,
    private val onItemClick: OnSpinnerItemClickListener,
    private val showDescription: Boolean,
    private val showImage: Boolean
) : RecyclerView.Adapter<SpinnerDialogSingleSelectAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView: View = LayoutInflater.from(context)
            .inflate(R.layout.item_spinner_dialog, parent, false)
        return ViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        i: Int
    ) {
        with(holder) {
            val model =
                arrData[this.adapterPosition]
            this.txtChoice.text = model.text

            this.radioButton.isChecked = model.isSelected
            this.contParentLayout.setOnClickListener { view: View? ->
                onItemClick.onItemClick(
                    this.adapterPosition,
                    model,
                    this@SpinnerDialogSingleSelectAdapter
                )
            }
        }

    }

    fun addItem(homeCategories: ArrayList<SpinnerModel>) {
        arrData = homeCategories
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var radioButton: RadioButton
        var txtChoice: TextView
        var contParentLayout: LinearLayoutCompat

        init {
            radioButton = itemView.findViewById(R.id.radioButton)
            txtChoice = itemView.findViewById(R.id.txtChoice)
            contParentLayout = itemView.findViewById(R.id.contParentLayout)
        }
    }

}
