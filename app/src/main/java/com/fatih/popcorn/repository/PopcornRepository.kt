package com.fatih.popcorn.repository

import com.fatih.popcorn.entities.remote.DiscoverResponse
import com.fatih.popcorn.movieapi.PopcornApi
import com.fatih.popcorn.other.Resource

class PopcornRepository (private val popcornApi: PopcornApi):PopcornRepositoryInterface {

    override suspend fun getMovies(sort_by: String, page: Int, genres: String): Resource<DiscoverResponse> {
        return try {
            val result=popcornApi.getMovies(sort_by = sort_by, page = page, genres = genres)
            if(result.isSuccessful){
                result.body()?.let {
                    Resource.success(it)
                }?: Resource.error("Response body empty")
            }else{
                Resource.error("Response failed")
            }
        }catch (e:Exception){
            Resource.error(e.message)
        }
    }

    override suspend fun getTvShows(sort_by: String, page: Int, genres: String): Resource<DiscoverResponse> {
        return try {
            val result=popcornApi.getTvShows(sort_by = sort_by, page = page, genres = genres)
            if(result.isSuccessful){
                result.body()?.let {
                    Resource.success(it)
                }?: Resource.error("Response body empty")
            }else{
                Resource.error("Response failed")
            }
        }catch (e:Exception){
            Resource.error(e.message)
        }
    }

    override suspend fun search(name: String, query: String, page: Int): Resource<DiscoverResponse> {
        return try {
            val result=popcornApi.search(name = name, query = query, page = page)
            if(result.isSuccessful){
                result.body()?.let {
                    Resource.success(it)
                }?: Resource.error("Response body empty")
            }else{
                Resource.error("Response failed")
            }
        }catch (e:Exception){
            Resource.error(e.message)
        }
    }
}