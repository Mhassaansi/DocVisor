package com.fictivestudios.docsvisor.apiManager.response

data class Content(
    val `data`: contentData,
    val message: String,
    val status: Int
)