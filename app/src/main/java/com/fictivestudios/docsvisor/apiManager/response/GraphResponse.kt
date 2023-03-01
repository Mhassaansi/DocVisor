package com.fictivestudios.docsvisor.apiManager.response

data class GraphResponse(
    val `data`: GraphData,
    val message: String,
    val status: Int
)