package com.fictivestudios.docsvisor.apiManager.response

data class ChatListResponse(
    val `data`: List<ChatListData>,
    val message: String,
    val status: Int
)