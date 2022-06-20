package com.example.testtask

data class TestCase(
    val N: Int,
    val Q: Int,
    val dictionary: List<List<String>>,
    val queries: List<List<String>>
)