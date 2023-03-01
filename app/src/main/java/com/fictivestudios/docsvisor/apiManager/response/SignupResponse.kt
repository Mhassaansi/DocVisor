package com.fictivestudios.docsvisor.apiManager.response

data class SignupResponse(
    val data: SignupData,
    val message: String,
    val status: Int
)