package com.fictivestudios.docsvisor.apiManager.response

data class CalibrationResponse(
    val `data`: CalibrationData,
    val message: String,
    val status: Int
)