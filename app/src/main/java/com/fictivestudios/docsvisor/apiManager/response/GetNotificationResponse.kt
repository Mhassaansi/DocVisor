package com.fictivestudios.docsvisor.apiManager.response

data class GetNotificationResponse(
    val `data`: List<GetNotificationData>,
    val message: String,
    val status: Int
)