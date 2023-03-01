package com.fictivestudios.docsvisor.adapter

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.apiManager.response.FitnessData
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.fragment.DoctorEditPatientProfile.Companion.noteText
import kotlinx.android.synthetic.main.item_edit_fitness.view.*

class FitnessAdapter(
    private val activity: Context?,
    private val arrData: List<FitnessData>,
    private val onItemClick: OnItemClickListener,
    private val isEnabled:Boolean
) : RecyclerView.Adapter<FitnessAdapter.ViewHolder>() {


    var isClick: Boolean = true
    var row_index =-1
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
            .inflate(R.layout.item_edit_fitness, parent, false)
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
            bindTo(holder.itemView,model,activity!!,isEnabled)
            setListener(this, model,arrData,i)
        }
        holder.itemView.txtMondayDes.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                noteText = holder.itemView.txtMondayDes.text.toString()
            }
        })
    }

    private fun setListener(holder: ViewHolder, model: FitnessData, arrData: List<FitnessData>, adapterPosition:Int) {


       // noteText=""

            holder.itemView.txtMondayImage.setOnClickListener { view ->


                onItemClick.onItemClick(
                    adapterPosition, model, holder.itemView.txtMondayDes,
                    FitnessAdapter::class.java.simpleName
                )


                row_index=adapterPosition;
                notifyDataSetChanged();


        }
        if(row_index==adapterPosition){
            isClick = false
            holder.itemView.txtMondayDes.visibility = View.VISIBLE
            holder.itemView.layoutMonday.background.setTint(activity?.resources!!.getColor(R.color.colorPrimary))
            holder.itemView.txt_heading.setTextColor(Color.parseColor("#ffffff"))
            holder.itemView.txtMondayImage.setColorFilter(activity?.resources!!.getColor(R.color.white))
            holder.itemView.txtMondayImage.rotation = 0F
        }
        else
        {
            holder.itemView.txt_heading.setTextColor(Color.parseColor("#6d7379"))
            holder.itemView.txtMondayImage.setColorFilter(activity?.resources!!.getColor(R.color.item_text))
            holder.itemView.txtMondayImage.rotation = 90F
            holder.itemView.layoutMonday.background.setTint(activity?.resources!!.getColor(R.color.white))
            holder.itemView.txtMondayDes.visibility = View.GONE
            isClick = true
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

        fun bindTo(itemView: View, model: FitnessData, context: Context, isEnabled: Boolean) {



            itemView.txtMondayDes.isEnabled = isEnabled
            itemView.txtMondayDes.isClickable = isEnabled
            itemView.txtMondayDes.isFocusable = isEnabled

            itemView.txt_heading.text = model?.day_name
            itemView.txtMondayDes.setText(model?.note)


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