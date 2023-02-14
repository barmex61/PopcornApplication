package com.fatih.popcorn.repository

import com.fatih.popcorn.entities.remote.DetailResponse
import com.fatih.popcorn.entities.remote.DiscoverResponse
import com.fatih.popcorn.other.Resource
import kotlinx.coroutines.flow.Flow

interface PopcornRepositoryInterface {

    suspend fun getMovies(sort_by:String, page:Int, genres:String):Resource<DiscoverResponse>
    suspend fun getTvShows(sort_by:String, page:Int, genres:String):Resource<DiscoverResponse>
    suspend fun search(name:String,query: String,page:Int):Resource<DiscoverResponse>
    fun getDetails(name:String,id:Int,language:String): Flow<Resource<DetailResponse>>

}