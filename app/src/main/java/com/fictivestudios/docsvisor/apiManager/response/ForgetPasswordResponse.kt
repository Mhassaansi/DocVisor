package com.fictivestudios.docsvisor.apiManager.response

data class ForgetPasswordResponse(
    val data: ForgetPasswordData,
    val message: String,
    val status: Int
)