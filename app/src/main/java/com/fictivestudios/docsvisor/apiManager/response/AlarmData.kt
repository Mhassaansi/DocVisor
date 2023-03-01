package com.fictivestudios.docsvisor.apiManager.response

data class AlarmData(
    val alaram_date: String,
        val alaram_time: String,
    val created_at: String,
    val id: Int,
    val note: String,
    val updated_at: String,
    val user_id: Int
)