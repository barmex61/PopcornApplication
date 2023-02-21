package com.fatih.popcorn.entities.remote.reviewresponse

data class AuthorDetails(
    val avatar_path: String,
    val name: String,
    val rating: Double,
    val username: String
)