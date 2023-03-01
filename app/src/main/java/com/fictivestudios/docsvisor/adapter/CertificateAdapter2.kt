package com.fictivestudios.docsvisor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.apiManager.response.Certification
import com.fictivestudios.docsvisor.callbacks.OnItemClickListener
import com.fictivestudios.docsvisor.enums.FileType
import com.fictivestudios.docsvisor.helper.FILE_TYPE_DOC
import com.fictivestudios.docsvisor.helper.FILE_TYPE_PHOTO
import com.fictivestudios.docsvisor.helper.MESSAGE_TYPE_MEDIA
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_certificate.view.*


class CertificateAdapter2(
    private val activity: Context?,
    private val arrData: List<Certification>,
    private val onItemClick: OnItemClickListener
) : RecyclerView.Adapter<CertificateAdapter2.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
            .inflate(R.layout.item_certificate, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val model = arrData[i]
        with(holder) {
            when (arrData[i].type) {
                FILE_TYPE_PHOTO -> {
                    Glide.with(activity!!)
                        .load(arrData[i].filename)
                        .placeholder(R.drawable.menu_gradient)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade(500))
                        .into(holder.ivImage)
                }
                FILE_TYPE_DOC -> {
                    Glide.with(activity!!)
                        .load(R.drawable.menu_gradient)
                        .placeholder(android.R.color.darker_gray)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade(500))
                        .into(holder.ivImage)
                }
            }
            holder.itemView.ivRemove.visibility = View.GONE

            //bindTo(model, activity!!,itemView)
            setListener(this, model)
        }
    }

    private fun setListener(holder: ViewHolder, model: Certification) {

        holder.ivImage.setOnClickListener { view ->
            onItemClick.onItemClick(
                holder.adapterPosition, model, view,
                CertificateAdapter2::class.java.simpleName
            )
        }
        holder.ivRemove.setOnClickListener { view ->
            onItemClick.onItemClick(
                holder.adapterPosition, model, view,
                CertificateAdapter2::class.java.simpleName
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
        fun bindTo(model: Certification, context: Context, itemView: View) {


            if (model.filename.isNullOrEmpty())
            {
                ivImage.setBackgroundResource(R.drawable.doctor)

            }
            else
            {
                Picasso.get().load(model.filename).placeholder(R.drawable.doctor).into(ivImage)
            }



        }
    }



}