package com.fictivestudios.docsvisor.model

data class PushAgoraData(
    val channel: String,
    val type: String,
    val user: String,
    val token: String,
    val user_name: String
)