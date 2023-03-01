package com.fictivestudios.docsvisor.callbacks

import com.fictivestudios.docsvisor.model.ChatItem
import java.util.*


interface ReceivedMessages {

    fun onMessageReceived(ChatList: ArrayList<ChatItem>)
}