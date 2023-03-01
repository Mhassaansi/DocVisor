package com.fictivestudios.docsvisor.apiManager.response

data class GetPatientProfileResponse(
    val `data`: PatientProfileData,
    val message: String,
    val status: Int
)