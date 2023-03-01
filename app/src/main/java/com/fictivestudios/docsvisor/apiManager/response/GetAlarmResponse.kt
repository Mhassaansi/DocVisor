package com.fictivestudios.docsvisor.apiManager.response

data class GetAlarmResponse(
    val `data`: List<AlarmData>,
    val message: String,
    val status: Int
)