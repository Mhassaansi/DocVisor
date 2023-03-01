package com.fictivestudios.docsvisor.apiManager.response

data class OffDaysRes(
    val `data`: List<OffDaysData>,
    val message: String,
    val status: Int
)