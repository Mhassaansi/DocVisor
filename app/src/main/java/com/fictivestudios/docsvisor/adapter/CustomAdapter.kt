package com.fictivestudios.docsvisor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.docsvisor.R

import com.fictivestudios.docsvisor.apiManager.response.AppResponseModel

class CustomAdapter : RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    var privatesesionnOBJ: ArrayList<AppResponseModel<Any>.Datum>
    var context: Context

    constructor(
        mprivatesesionnOBJ: ArrayList<AppResponseModel<Any>.Datum>,
        context: Context
    ) {
        this.context = context
        this.privatesesionnOBJ = mprivatesesionnOBJ
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v: View = inflater.inflate(R.layout.sample_item_view, parent, false)
        val vh: CustomAdapter.ViewHolder = CustomAdapter.ViewHolder(v, context)
        return vh
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.paramsData1.text=privatesesionnOBJ.get(position).data1
        holder.paramsData2.text=privatesesionnOBJ.get(position).data2
        holder.paramsData3.text=privatesesionnOBJ.get(position).data3
        holder.paramsData4.text=privatesesionnOBJ.get(position).data4
        holder.paramsData5.text=privatesesionnOBJ.get(position).data5
        holder.paramsData6.text=privatesesionnOBJ.get(position).data6
        holder.paramsData7.text=privatesesionnOBJ.get(position).data7
        holder.paramsData8.text=privatesesionnOBJ.get(position).data8
        holder.paramsData9.text=privatesesionnOBJ.get(position).data9
        holder.paramsData10.text=privatesesionnOBJ.get(position).data10


        var pos=holder.adapterPosition
        holder.paramsIndex.text=pos.toString()

    }


    class ViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {
        lateinit var paramsName: TextView
        lateinit var paramsData1: TextView
        lateinit var paramsData2: TextView
        lateinit var paramsData3: TextView
        lateinit var paramsData4: TextView
        lateinit var paramsData5: TextView
        lateinit var paramsData6: TextView
        lateinit var paramsData7: TextView
        lateinit var paramsData8: TextView
        lateinit var paramsData9: TextView
        lateinit var paramsData10: TextView
        lateinit var paramsIndex: TextView

        init {
            paramsData1=itemView.findViewById(R.id.params_data1)
            paramsData2=itemView.findViewById(R.id.params_data2)
            paramsData3=itemView.findViewById(R.id.params_data3)
            paramsData4=itemView.findViewById(R.id.params_data4)
            paramsData5=itemView.findViewById(R.id.params_data5)
            paramsData6=itemView.findViewById(R.id.params_data6)
            paramsData7=itemView.findViewById(R.id.params_data7)
            paramsData8=itemView.findViewById(R.id.params_data8)
            paramsData9=itemView.findViewById(R.id.params_data9)
            paramsData10=itemView.findViewById(R.id.params_data10)


            paramsName=itemView.findViewById(R.id.params_name)
            paramsIndex=itemView.findViewById(R.id.params_index)
        }

    }

    override fun getItemCount(): Int {
        return privatesesionnOBJ.size
    }
}
