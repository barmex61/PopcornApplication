package com.fatih.popcorn.repository

import com.fatih.popcorn.BuildConfig
import com.fatih.popcorn.entities.remote.detailresponse.DetailResponse
import com.fatih.popcorn.entities.remote.discoverresponse.DiscoverResponse
import com.fatih.popcorn.entities.remote.imageresponse.ImageResponse
import com.fatih.popcorn.other.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Path
import retrofit2.http.Query

interface PopcornRepositoryInterface {

    suspend fun getMovies(sort_by:String, page:Int, genres:String):Resource<DiscoverResponse>
    suspend fun getTvShows(sort_by:String, page:Int, genres:String):Resource<DiscoverResponse>
    suspend fun search(name:String,query: String,page:Int):Resource<DiscoverResponse>
    fun getDetails(name:String,id:Int,language:String): Flow<Resource<DetailResponse>>
    suspend fun getImages( name:String, id:Int ): Response<ImageResponse>

}