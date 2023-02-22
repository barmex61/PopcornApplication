package com.fatih.popcorn.entities.remote.reviewresponse

data class ReviewResponse(
    val id: Int,
    val page: Int,
    var results: List<ReviewResult>,
    val total_pages: Int,
    val total_results: Int
)