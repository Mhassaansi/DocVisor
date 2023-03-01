package com.fictivestudios.docsvisor.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.ui.text.toLowerCase
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.apiManager.response.doctorthird.Result
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import java.util.*
import kotlin.collections.ArrayList
class DoctorListThirdPartyAdapter(
    private val activity: Context?,
    private var mList: ArrayList<Result>,
    private val onItemClick: OnItemClickListener
): RecyclerView.Adapter<DoctorListThirdPartyAdapter.ViewHolder>(),Filterable {
    var filteredList = ArrayList<Result>()
    init {
        filteredList = mList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DoctorListThirdPartyAdapter.ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
            .inflate(R.layout.item_doctor_third_party, parent, false)
        return DoctorListThirdPartyAdapter.ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: DoctorListThirdPartyAdapter.ViewHolder, position: Int) {
        holder.bindTo(mList.get(position))
    }
    override fun getItemCount(): Int {
        return mList.size
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<AppCompatTextView>(R.id.ttv_name)
        val tvGender = view.findViewById<AppCompatTextView>(R.id.ttv_gender)
        val tvPhoneNumber = view.findViewById<AppCompatTextView>(R.id.ttv_phone)
        val tvAddress = view.findViewById<AppCompatTextView>(R.id.ttv_address)
        val npi = view.findViewById<AppCompatTextView>(R.id.npi)
        val pri_spec = view.findViewById<AppCompatTextView>(R.id.pri_spec)
        val hosp_afl_lbn_1 = view.findViewById<AppCompatTextView>(R.id.hosp_afl_lbn_1)
        val hosp_afl_lbn_2 = view.findViewById<AppCompatTextView>(R.id.hosp_afl_lbn_2)
        val hosp_afl_lbn_3 = view.findViewById<AppCompatTextView>(R.id.hosp_afl_lbn_3)
        val hosp_afl_lbn_4 = view.findViewById<AppCompatTextView>(R.id.hosp_afl_lbn_4)
        val hosp_afl_lbn_5 = view.findViewById<AppCompatTextView>(R.id.hosp_afl_lbn_5)
        var model: String? = null

        fun bindTo(model: Result) {
            tvName.setText("Name  :  "+model.frst_nm+ " "+model.mid_nm+ " "+model.lst_nm)
            tvAddress.setText("Address  :  "+model.adr_ln_1+ " "+model.adr_ln_2+" "+model.cty+" "+model.st+" "+model.zip)
            npi.setText("License number  :   "+model.npi)
            if(model.gndr=="M"){
                tvGender.setText("Gender  :  "+"Male")
            }
            else{
                tvGender.setText("Gender  :  "+"Female")
            }
            if(model.pri_spec != ""){
                pri_spec.setText("Specialization  :  "+model.pri_spec)
            }
            else{
                pri_spec.visibility = View.GONE
            }
            if(model.phn_numbr != ""){
                tvPhoneNumber.visibility =View.VISIBLE
                tvPhoneNumber.setText("Phone Number  :  "+model.phn_numbr)
            }else{
                tvPhoneNumber.visibility =View.GONE
            }
            if(model.hosp_afl_lbn_1 != ""){
                hosp_afl_lbn_1.visibility = View.VISIBLE
                hosp_afl_lbn_1.setText("Hospital 1  :  "+model.hosp_afl_lbn_1)
            }
            else{
            }
            if(model.hosp_afl_lbn_2 != ""){
                hosp_afl_lbn_2.visibility = View.VISIBLE
                hosp_afl_lbn_2.setText("Hospital 2  :  "+model.hosp_afl_lbn_2)
            }
            else{
                hosp_afl_lbn_2.visibility = View.GONE
            }
            if(model.hosp_afl_lbn_3 != ""){
                hosp_afl_lbn_3.visibility = View.VISIBLE
                hosp_afl_lbn_3.setText("Hospital 3  :  "+model.hosp_afl_lbn_3)
            }
            else{
                hosp_afl_lbn_3.visibility = View.GONE
            }
            if(model.hosp_afl_lbn_4 != ""){
                hosp_afl_lbn_4.visibility = View.VISIBLE
                hosp_afl_lbn_4.setText("Hospital 4  :  "+model.hosp_afl_lbn_4)
            }
            else{
                hosp_afl_lbn_4.visibility = View.GONE
            }
            if(model.hosp_afl_lbn_5 != ""){
                hosp_afl_lbn_5.visibility = View.VISIBLE
                hosp_afl_lbn_5.setText("Hospital 5  :  "+model.hosp_afl_lbn_5)
            }
            else{
                hosp_afl_lbn_5.visibility = View.GONE
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
                    val resultList = ArrayList<Result>()
                    for (row in filteredList) {
                        if (row.frst_nm.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT)) == true) {
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
                mList = results?.values as ArrayList<Result>
                notifyDataSetChanged()
            }
        }
    }
}
