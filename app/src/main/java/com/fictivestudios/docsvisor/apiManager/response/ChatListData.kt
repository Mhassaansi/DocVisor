package com.fictivestudios.docsvisor.apiManager.response

data class ChatListData(
    val msg: String,
    val name: String,
    val sender_name: String,
    val userID: Int,
    val user_image: String
)