package com.fictivestudios.docsvisor.apiManager.response

import com.fictivestudios.docsvisor.apiManager.requestBody.ChatMediaData

data class ChatMediaResponse(
    val `data`: ChatMediaData,
    val status: Int,
    val message: String
)