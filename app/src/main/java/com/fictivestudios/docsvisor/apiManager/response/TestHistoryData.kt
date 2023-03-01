package com.fictivestudios.docsvisor.apiManager.response

data class TestHistoryData(
    val report: List<Report>,
    val tests: HashMap<String,String>
)