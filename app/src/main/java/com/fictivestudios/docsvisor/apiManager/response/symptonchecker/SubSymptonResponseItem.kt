package com.fictivestudios.docsvisor.apiManager.response.symptonchecker

data class SubSymptonResponseItem(
    val HasRedFlag: Boolean,
    val HealthSymptomLocationIDs: List<Int>,
    val ID: Int,
    val Name: String,
    val ProfName: String,
    val Synonyms: List<String>
)