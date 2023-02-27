package com.fatih.popcorn.entities.remote.imageresponse

data class ImageResponse(
    val backdrops: List<Backdrop>,
    val id: Int,
    val posters: List<Poster>
)