package com.fictivestudios.docsvisor.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.apiManager.response.DoctorData
import com.fictivestudios.docsvisor.apiManager.response.doctorthird.Result
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.helper.doctorData
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList

class DoctorListAdapter(
    private val activity: Context?,
    private var arrData: ArrayList<DoctorData>,
    private val onItemClick: OnItemClickListener
) : RecyclerView.Adapter<DoctorListAdapter.ViewHolder>() , Filterable {

    var filteredList = ArrayList<DoctorData>()
    init {
     //   as ArrayList<DoctorData>
        filteredList = arrData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
            .inflate(R.layout.item_doctor_list, parent, false)
        return DoctorListAdapter.ViewHolder(itemView)


    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        /*val model = arrData[i]
        with(holder) {
            bindTo(model, activity!!)
            setListener(this, model)
        }*/

        val model = arrData[i]
        with(holder) {
            bindTo(model, activity!!)

            setListener(this, model)
        }
    }

    private fun setListener(holder: ViewHolder, model: DoctorData) {

        holder.run {
            holder.itemView.setOnClickListener { view ->
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

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<AppCompatTextView>(R.id.tv_name)
        val tvProfession = view.findViewById<AppCompatTextView>(R.id.tv_profession)
        val iv_image = view.findViewById<CircleImageView>(R.id.imgLastActivityUser)


        var model: String? = null

/*        *
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.*/

        fun bindTo(model: DoctorData, context: Context) {


            tvName.text = model.name
            tvProfession.text = model.profession


            if (model?.image.isNullOrEmpty())
            {

                iv_image.setBackgroundResource(R.drawable.user)
            }
            else
            {
               Picasso.get().load(model?.image).placeholder(R.drawable.user) .into(iv_image)
               // Log.d("images",model?.image.toString())
            }

        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                val charSearch = constraint.toString()
                if (charSearch== null || charSearch.isEmpty()) {
                    // filteredList = mList
                    filterResults.count = filteredList.size
                    filterResults.values = filteredList
                } else {
                    val resultList = ArrayList<DoctorData>()
                    for (row in filteredList) {
                        if (row.name.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT)) == true) {
                            resultList.add(row)
                        }
                    }
                    filterResults.count = resultList.size
                    filterResults.values = resultList
                }
                return filterResults
            }
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                arrData = results?.values as ArrayList<DoctorData>
                notifyDataSetChanged()
            }
        }
    }
}