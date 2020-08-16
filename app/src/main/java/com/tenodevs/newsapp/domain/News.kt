package com.tenodevs.newsapp.domain

data class News(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)