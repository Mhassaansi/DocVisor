package com.fictivestudios.docsvisor.model

import com.fictivestudios.docsvisor.apiManager.response.Certification

data class User(
    val age: Int,
    val certifications: List<Certification>,
    val country_code: String,
    val date_of_birth: String,
    val email: String,
    val gender: String,
    val height: String,
    val id: Int,
    val image: String,
    val is_verified: Int,
    val name: String,
    val phone_no: String,
    val profession: String,
    val profile_completed: Int,
    val role: String,
    val watch_calibration: Int,
    val watch_connectivity: Int,
    val watch_imei: String,
    val watch_user_id: String,
    val weight: String,
    val bio: String
)