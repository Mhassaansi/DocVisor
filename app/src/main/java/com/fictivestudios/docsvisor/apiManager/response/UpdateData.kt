package com.fictivestudios.docsvisor.apiManager.response

data class UpdateData(
    val age: Int,
    val bio: String,
    val certifications: List<Any>,
    val date_of_birth: String,
    val email: String,
    val gender: String,
    val id: Int,
    val image: String,
    val is_verified: Int,
    val name: String,
    val phone_no: String,
    val profession: String,
    val profile_completed: Int,
    val role: String
)