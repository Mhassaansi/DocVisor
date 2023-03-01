package com.fictivestudios.docsvisor.adapter

import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.apiManager.response.ChatListData
import com.fictivestudios.docsvisor.helper.MESSAGE_TYPE_MEDIA
import com.fictivestudios.docsvisor.helper.MESSAGE_TYPE_TEXT
import com.fictivestudios.docsvisor.helper.VIEW_TYPE_MESSAGE_RECEIVED
import com.fictivestudios.docsvisor.helper.VIEW_TYPE_MESSAGE_SENT
import com.fictivestudios.docsvisor.model.ChatItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_mychat.view.*
import kotlinx.android.synthetic.main.item_otherchat.view.*
import java.text.SimpleDateFormat

class ConversationAdapter(context:Context, chatList: List<ChatItem>, userId:String)  : RecyclerView.Adapter<ConversationAdapter.ProfileViewHolder>() {

    private var chatList: List<ChatItem>? = chatList
    private val userId: String? = userId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ProfileViewHolder{


        val view:View

        if (viewType == VIEW_TYPE_MESSAGE_RECEIVED)
        {
             view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_otherchat, parent, false)
        }
        else
        {
             view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_mychat, parent, false)
        }


        return ProfileViewHolder(view)


    }


    override fun getItemViewType(position: Int): Int {
        return if (userId.toString().equals(chatList?.get(position)!!.sender_id))
        {
            VIEW_TYPE_MESSAGE_SENT
        } else {
            VIEW_TYPE_MESSAGE_RECEIVED
        }
    }

    override fun getItemCount() = chatList?.size ?: 1

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {




        if (userId.toString().equals(chatList?.get(position)?.sender_id))
        {

            if (chatList?.get(position)?.type == MESSAGE_TYPE_MEDIA)
            {
                holder.itemView.iv_media.visibility = View.VISIBLE

                if (chatList?.get(position)?.message?.isNullOrBlank() != true)
                {
                    Picasso.get().load(chatList?.get(position)?.message).into(holder.itemView.iv_media)
                }
            }
            else if (chatList?.get(position)?.type == MESSAGE_TYPE_TEXT)
            {  holder.itemView.iv_media.visibility = View.GONE
                holder.itemView.tv_message_sent .text = chatList?.get(position)?.message.toString()

            }

        //    holder.itemView.timesent.text = chatList!!.get(position)?.updated_at.getTime("yyyy-MM-dd'T'HH:ss:SSS","HH:ss a")

        }
        else
        {
            if (chatList?.get(position)?.type == MESSAGE_TYPE_MEDIA)
            {
                holder.itemView.iv_media_received.visibility = View.VISIBLE

                if (chatList?.get(position)?.message?.isNullOrBlank() != true)
                {
                    Picasso.get().load(chatList?.get(position)?.message).into(holder.itemView.iv_media_received)
                }
            }
            else if (chatList?.get(position)?.type == MESSAGE_TYPE_TEXT)
            {
                holder.itemView.iv_media_received.visibility = View.GONE
                holder.itemView.tv_message .text = chatList?.get(position)?.message.toString()
            }
            else
            {
                holder.itemView.iv_media_received.visibility = View.VISIBLE

                holder.itemView.iv_media_received.setBackgroundResource(R.drawable.icon_document)

            }
          //  holder.itemView.time.text = chatList!!.get(position)?.updated_at.getTime("yyyy-MM-dd'T'HH:ss:SSS","HH:ss a")

        }



    }


    fun setProfiles(profiles: List<ContactsContract.Profile>) {
       /* this.profiles = profiles
        notifyDataSetChanged()*/
    }

     class ProfileViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {

    }
    fun String.getTime(input: String, output: String): String {
        val inputFormat = SimpleDateFormat(input)
        val outputFormat = SimpleDateFormat(output)
        val date = inputFormat.parse(this)
        return outputFormat.format(date ?: "")
    }
}