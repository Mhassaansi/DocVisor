package com.fictivestudios.docsvisor.apiManager.response.symptonchecker

data class Issue(
    val Accuracy: Double,
    val ID: Int,
    val Icd: String,
    val IcdName: String,
    val Name: String,
    val ProfName: String,
    val Ranking: Int
)