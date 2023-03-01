package com.fictivestudios.docsvisor.model

data class PushNotification(
    val body: String,
    val postId: String,
    val title: String,
    val type: String
)