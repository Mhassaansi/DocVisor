package com.fictivestudios.docsvisor.apiManager.response

data class PendingAppointmentData(
    val appointment_date: String,
    val appointment_time: String,
    val id: Int,
    val image: String,
    val username: String
)