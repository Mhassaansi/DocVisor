package com.fictivestudios.docsvisor.apiManager.response

data class TestHistoryResponse(
    val `data`: TestHistoryData,
    val message: String,
    val status: Int
)