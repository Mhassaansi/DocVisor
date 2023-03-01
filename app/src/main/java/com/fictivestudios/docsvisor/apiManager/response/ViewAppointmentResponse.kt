package com.fictivestudios.docsvisor.apiManager.response

data class ViewAppointmentResponse(
    val `data`: List<AppointmentData>,
    val message: String,
    val status: Int
)