package com.fictivestudios.docsvisor.apiManager.response

data class GraphData(
    val average_value: Int,
    val `data`: List<TestResult>,
    val high_test_count: Int,
    val highest_value: Int,
    val low_test_count: Int,
    val lowest_value: Int,
    val normal_test_count: Int,
    val total_test_count: Int
)