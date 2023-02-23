package com.fatih.popcorn.entities.remote.youtuberesponse


import com.google.gson.annotations.SerializedName

data class Standard(
    val height: Int,
    val url: String,
    val width: Int
)