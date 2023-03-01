package com.fictivestudios.docsvisor.apiManager.response

import com.fictivestudios.docsvisor.model.User

data class UpdateResponse(
    val data: User,
    val message: String,
    val status: Int
)