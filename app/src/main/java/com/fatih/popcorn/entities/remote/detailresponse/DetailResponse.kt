package com.fatih.popcorn.entities.remote.detailresponse

import java.io.Serializable

data class DetailResponse(
    val adult: Boolean?,
    val backdrop_path: String?,
    val belongs_to_collection: BelongsToCollection?,
    val budget: Int?,
    val original_title: String?,
    val episode_run_time: List<Int>?,
    val first_air_date: String?,
    val genres: List<Genre>?,
    val id: Int?,
    val last_air_date: String?,
    val name: String?,
    val number_of_episodes: Int?,
    val number_of_seasons: Int?,
    val origin_country: List<String>?,
    val original_name: String?,
    val seasons: List<Season>?,
    val original_language: String?,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,
    val production_companies: List<ProductionCompany>?,
    val production_countries: List<ProductionCountry>?,
    val release_date: String?,
    val revenue: Long?,
    val runtime: Int?,
    val status: String?,
    val tagline: String?,
    val title: String?,
    val vote_average: Double?,
    val vote_count: Int?
) : Serializable