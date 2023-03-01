package com.fictivestudios.docsvisor.apiManager.response.doctorthird

data class Query(
    var count: Boolean,
    var format: String,
    var keys: Boolean,
    var limit: Int,
    var offset: Int,
    var properties: List<String>,
    var resources: List<Resource>,
    var results: Boolean,
    var rowIds: Boolean,
    var schema: Boolean
)