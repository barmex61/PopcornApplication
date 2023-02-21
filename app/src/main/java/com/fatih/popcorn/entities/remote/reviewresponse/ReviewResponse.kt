package com.fatih.popcorn.entities.remote.reviewresponse

data class ReviewResponse(
    val id: Int,
    val page: Int,
    val results: List<ReviewResult>,
    val total_pages: Int,
    val total_results: Int
)