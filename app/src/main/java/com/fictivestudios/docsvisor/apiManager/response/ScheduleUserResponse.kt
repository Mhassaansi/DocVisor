package com.fictivestudios.docsvisor.apiManager.response

data class ScheduleUserResponse(
    val `data`: List<Data>,
    val message: String,
    val status: Int
)