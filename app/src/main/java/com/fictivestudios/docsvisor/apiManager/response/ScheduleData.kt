package com.fictivestudios.docsvisor.apiManager.response

data class ScheduleData(
    val day: String,
    val from_time: String,
    val status: Int,
    val to_time: String
)