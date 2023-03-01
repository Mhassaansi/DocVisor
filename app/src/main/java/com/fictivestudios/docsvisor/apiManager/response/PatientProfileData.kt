package com.fictivestudios.docsvisor.apiManager.response

data class PatientProfileData(
    val diet: List<FitnessData>,
    val email: String,
    val fitness: List<FitnessData>,
    val id: Int,
    val image: String,
    val medics: List<FitnessData>,
    val tests: HashMap<String,String>,
    val username: String
)