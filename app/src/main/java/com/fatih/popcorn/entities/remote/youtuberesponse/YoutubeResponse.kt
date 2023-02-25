package com.fatih.popcorn.entities.remote.youtuberesponse

data class YoutubeResponse(
    val etag: String,
    var items: List<İtem>,
    val kind: String,
    val pageInfo: PageInfo
)