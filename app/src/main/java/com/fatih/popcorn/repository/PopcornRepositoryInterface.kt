package com.fatih.popcorn.repository

import com.fatih.popcorn.entities.remote.movie.MovieResponse
import com.fatih.popcorn.entities.remote.movie.MovieResult
import com.fatih.popcorn.entities.remote.tvshow.TvShowResponse
import com.fatih.popcorn.entities.remote.tvshow.TvShowResult
import com.fatih.popcorn.other.Resource

interface PopcornRepositoryInterface {

    suspend fun getMovies(sort_by:String, page:Int, genres:String):Resource<MovieResponse>
    suspend fun getTvShows(sort_by:String, page:Int, genres:String):Resource<TvShowResponse>

}