package com.fictivestudios.docsvisor.apiManager.response.symptonchecker

data class DiagonisResponseItem(
    val Issue: Issue,
    val Specialisation: List<Specialisation>
)