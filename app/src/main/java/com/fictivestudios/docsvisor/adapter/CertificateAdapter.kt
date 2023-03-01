package com.fictivestudios.docsvisor.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.enums.FileType
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList

class CertificateAdapter(
    private val activity: Context?,
    private val arrData: ArrayList<Uri>,
    private val onItemClick: OnItemClickListener,
    private val type: FileType
) : RecyclerView.Adapter<CertificateAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
            .inflate(R.layout.item_certificate, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val model = arrData[i]
        with(holder) {
            when (type) {
                FileType.IMAGE -> {
                    Glide.with(activity!!)
                        .load(arrData[position])
                        .placeholder(R.drawable.menu_gradient)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade(500))
                        .into(holder.ivImage)
                }
                FileType.DOCUMENT -> {
                    Glide.with(activity!!)
                        .load(R.drawable.menu_gradient)
                        .placeholder(android.R.color.darker_gray)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade(500))
                        .into(holder.ivImage)
                }
            }
            bindTo(model, activity!!,itemView)
            setListener(this, model)
        }
    }

    private fun setListener(holder: ViewHolder, model: Uri) {

        holder.ivImage.setOnClickListener { view ->
            onItemClick.onItemClick(
                holder.adapterPosition, model, view,
                CertificateAdapter::class.java.simpleName
            )
        }
        holder.ivRemove.setOnClickListener { view ->
            onItemClick.onItemClick(
                holder.adapterPosition, model, view,
                CertificateAdapter::class.java.simpleName
            )
        }

    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivImage: ImageView = view.findViewById<AppCompatImageView>(R.id.ivImage)
        var ivRemove: ImageView = view.findViewById<AppCompatImageView>(R.id.ivRemove)
        var model: String? = null

        /**
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.
         */
        fun bindTo(model: Uri, context: Context, itemView: View) {

            Picasso.get().load(model).placeholder(R.drawable.doctor).into(ivImage)


        }
    }

}