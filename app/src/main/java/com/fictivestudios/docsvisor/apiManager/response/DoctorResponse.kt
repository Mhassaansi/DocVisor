package com.fictivestudios.docsvisor.apiManager.response

data class DoctorResponse(
    val `data`: ArrayList<DoctorData>,
    val message: String,
    val status: Int
)