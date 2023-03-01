package com.fictivestudios.docsvisor.apiManager.response.doctorthird

data class DoctorThridPartApiModel(
    var count: Int,
    var query: Query,
    var results: ArrayList<Result>,
    var schema: Schema
)