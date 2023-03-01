package com.fictivestudios.docsvisor.apiManager.response.symptonchecker

data class IssuesInfoResponse(
    val Description: String,
    val DescriptionShort: String,
    val MedicalCondition: String,
    val Name: String,
    val PossibleSymptoms: String,
    val ProfName: String,
    val Synonyms: Any,
    val TreatmentDescription: String
)