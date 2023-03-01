package com.fictivestudios.docsvisor.apiManager.response

data class ResendOTPResponse(
    val data: ResendOTPData,
    val message: String,
    val status: Int
)