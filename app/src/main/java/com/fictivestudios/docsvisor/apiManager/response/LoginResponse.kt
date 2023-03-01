package com.fictivestudios.docsvisor.apiManager.response

import com.fictivestudios.docsvisor.model.User

data class LoginResponse(
    val bearer_token: String,
    val `data`: User,
    val message: String,
    val status: Int
)