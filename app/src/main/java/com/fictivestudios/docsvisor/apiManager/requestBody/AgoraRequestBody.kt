package com.fictivestudios.docsvisor.apiManager.requestBody

data class AgoraRequestBody(
    val name: String,
    val reciever_id: Int,
    val sender_id: Int,
    val type: String
)