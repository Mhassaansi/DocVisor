package com.fictivestudios.docsvisor.model

data class RejectCallRequestBody(
    val sender_id : String,
    val reciever_id: String,
    val channel: String,
    val type: String
)
