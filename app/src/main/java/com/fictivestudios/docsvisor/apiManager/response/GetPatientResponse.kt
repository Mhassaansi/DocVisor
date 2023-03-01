package com.fictivestudios.docsvisor.apiManager.response

data class GetPatientResponse(
    val `data`: List<PatientData>,
    val message: String,
    val status: Int
)