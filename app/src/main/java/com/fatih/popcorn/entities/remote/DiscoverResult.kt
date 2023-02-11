package com.fatih.popcorn.entities.remote

data class DiscoverResult(
    val adult: Boolean?,
    val backdrop_path: String?,
    val first_air_date: String?,
    val genre_ids: List<Int>?,
    val id: Int?,
    val name: String?,
    val origin_country: List<String>?,
    val original_language: String?,
    val original_name: String?,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,
    val vote_average: Double?,
    val vote_count: Int?,
    val video: Boolean?,
    val title: String?,
    val release_date: String?,
    val original_title: String?,

    )