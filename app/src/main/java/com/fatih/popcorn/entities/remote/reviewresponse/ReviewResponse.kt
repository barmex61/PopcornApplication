package com.fatih.popcorn.entities.remote.reviewresponse

data class ReviewResponse(
    var results: List<ReviewResult>,
    val total_results: Int
)