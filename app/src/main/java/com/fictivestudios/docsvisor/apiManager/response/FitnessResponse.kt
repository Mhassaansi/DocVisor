package com.fictivestudios.docsvisor.apiManager.response

data class FitnessResponse(
    val `data`: List<FitnessData>,
    val message: String,
    val status: Int
)