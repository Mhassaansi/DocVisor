package com.fictivestudios.docsvisor.apiManager.response

data class DoctorData(
    val bio: String,
    val certifications: List<Certification>,
    val email: String,
    val id: Int,
    val image: String,
    val name: String,
    val phone_no: String,
    val profession: String
)