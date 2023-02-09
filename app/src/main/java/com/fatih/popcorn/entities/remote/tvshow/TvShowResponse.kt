package com.fatih.popcorn.entities.remote.tvshow

data class TvShowResponse(
    val page: Int,
    val results: List<TvShowResult>,
    val total_pages: Int,
    val total_results: Int
)