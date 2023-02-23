package com.fatih.popcorn.entities.remote.youtuberesponse


data class İtem(
    val contentDetails: ContentDetails,
    val etag: String,
    val id: String,
    val kind: String,
    val snippet: Snippet,
    val statistics: Statistics
)