package com.fictivestudios.docsvisor.apiManager.response

data class AppointmentData(
    val appointment_date: String,
    val appointment_time: String,
    val id: Int,
    val image: String,
    val username: String,
    val status: String


)