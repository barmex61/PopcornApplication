package com.fatih.popcorn.movieapi

import com.fatih.popcorn.BuildConfig
import com.fatih.popcorn.entities.remote.DetailResponse
import com.fatih.popcorn.entities.remote.DiscoverResponse

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PopcornApi {
    //https://api.themoviedb.org/3/movie/505642?api_key=ae624ef782f69d5092464dffa234178b&language=en-US
    //https://youtube.googleapis.com/youtube/v3/videos?part=snippet%2CcontentDetails%2Cstatistics&id=Ks-_Mh1QhMc%2Cc0KYU2j0TM4%2CeIho2S0ZahI&key=AIzaSyDjn7FjfG2kUgWP4D5-w1GoigWWyyT_ZQs
    //https://api.themoviedb.org/3/discover/tv?api_key=ae624ef782f69d5092464dffa234178b&sort_by=popularity.desc&page=1
    //https://api.themoviedb.org/3/search/movie?api_key=ae624ef782f69d5092464dffa234178b&query=Spi&page=1
    //https://api.themoviedb.org/3/tv/85552/videos?api_key=ae624ef782f69d5092464dffa234178b
    //https://api.themoviedb.org/3/discover/movie?api_key=ae624ef782f69d5092464dffa234178b&sort_by=popularity.desc&page=1&with_genres=80
    @GET("discover/movie")
    suspend fun getMovies(
        @Query("api_key") api_key:String=BuildConfig.API_KEY,
        @Query("sort_by") sort_by:String,
        @Query("page") page:Int,
        @Query("with_genres") genres:String
    ):Response<DiscoverResponse>

    @GET("discover/tv")
    suspend fun getTvShows(
        @Query("api_key") api_key:String=BuildConfig.API_KEY,
        @Query("sort_by") sort_by:String,
        @Query("page") page:Int,
        @Query("with_genres") genres:String
    ):Response<DiscoverResponse>

    @GET("search/{name}")
    suspend fun search(
        @Path("name") name:String,
        @Query("api_key") api_key: String = BuildConfig.API_KEY,
        @Query("query") query: String,
        @Query("page") page:Int):Response<DiscoverResponse>

    @GET("{name}/{id}")
    suspend fun getDetails(@Path("name") searchName:String,
                           @Path("id") id:Int,
                           @Query("api_key") api_key:String=BuildConfig.API_KEY,
                           @Query("language") language:String ):Response<DetailResponse>

}