package com.fictivestudios.docsvisor.apiManager.response

data class GetNotificationData(
    val created_at: String,
    val `data`: String,
    val description: String,
    val from_user_id: Int,
    val id: Int,
    val model_type_id: Int,
    val notification_type: String,
    val title: String,
    val to_user_id: Int,
    val updated_at: String
)