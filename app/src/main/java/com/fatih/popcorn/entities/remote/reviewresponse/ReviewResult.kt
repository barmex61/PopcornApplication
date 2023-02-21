package com.fatih.popcorn.entities.remote.reviewresponse

data class ReviewResult(
    val author: String,
    val author_details: AuthorDetails,
    val content: String,
    val created_at: String,
    val id: String,
    val updated_at: String,
    val url: String
)