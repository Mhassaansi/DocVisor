package com.fictivestudios.docsvisor.apiManager.response

data class GetSchedule(
    val `data`: List<ScheduleData>,
    val message: String,
    val status: Int
)