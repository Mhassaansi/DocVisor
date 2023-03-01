package com.fictivestudios.docsvisor.apiManager.response

data class PendingAppointment(
    val `data`: List<PendingAppointmentData>,
    val message: String,
    val status: Int
)