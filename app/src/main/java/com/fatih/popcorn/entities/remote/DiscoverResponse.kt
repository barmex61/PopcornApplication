package com.fatih.popcorn.entities.remote

data class DiscoverResponse(
    var page: Int,
    var results: List<DiscoverResult>,
    var total_pages: Int,
    var total_results: Int
)