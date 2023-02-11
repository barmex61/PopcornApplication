package com.fatih.popcorn.entities.remote

data class DiscoverResponse(
    val page: Int,
    val results: List<DiscoverResult>,
    val total_pages: Int,
    val total_results: Int
)