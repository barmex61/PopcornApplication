package com.fatih.popcorn.movieapi

import com.fatih.popcorn.BuildConfig
import com.fatih.popcorn.BuildConfig.API_KEY
import com.fatih.popcorn.BuildConfig.YOUTUBE_API_KEY
import com.fatih.popcorn.entities.remote.creditsresponse.CreditsResponse
import com.fatih.popcorn.entities.remote.detailresponse.DetailResponse
import com.fatih.popcorn.entities.remote.discoverresponse.DiscoverResponse
import com.fatih.popcorn.entities.remote.imageresponse.ImageResponse
import com.fatih.popcorn.entities.remote.reviewresponse.ReviewResponse
import com.fatih.popcorn.entities.remote.videoresponse.VideoResponse
import com.fatih.popcorn.entities.remote.youtuberesponse.YoutubeResponse

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PopcornApi {
    

    @GET("{name}/{id}/videos")
    suspend fun getVideos(
        @Path("name")name:String,
        @Path("id")id:Int,
        @Query("api_key") api_key: String =API_KEY):Response<VideoResponse>
    @GET("{name}/{id}/similar")
    suspend fun getFamiliar(
        @Path("name") name:String,
        @Path("id") id:Int,
        @Query("api_key") api_key: String= API_KEY,
        @Query("page") page:Int):Response<DiscoverResponse>
    @GET("{name}/{id}/recommendations")
    suspend fun getRecommendations(
        @Path("name") name:String,
        @Path("id") id:Int,
        @Query("api_key") api_key: String =  API_KEY,
        @Query("page") page:Int):Response<DiscoverResponse>

    @GET("{name}/{id}/reviews")
    suspend fun getReviews(
       @Path("name") name:String,
       @Path("id") id:Int,
       @Query("api_key") api_key: String =  API_KEY,
       @Query("page") page:Int):Response<ReviewResponse>

    @GET("{name}/{id}/credits")
    suspend fun getCredits(
       @Path("name") name: String,
       @Path("id") id: Int,
       @Query("api_key") api_key: String = API_KEY):Response<CreditsResponse>

    @GET("{name}/{id}/watch/providers")
    suspend fun getWatchProviders(
        @Path("name") name:String,
        @Path("id") id:Int,
        @Query("api_key") api_key: String =  API_KEY):Response<String>


    @GET("discover/movie")
    suspend fun getMovies(
        @Query("api_key") api_key:String= API_KEY,
        @Query("sort_by") sort_by:String,
        @Query("page") page:Int,
        @Query("with_genres") genres:String
    ):Response<DiscoverResponse>

    @GET("discover/tv")
    suspend fun getTvShows(
        @Query("api_key") api_key:String= API_KEY,
        @Query("sort_by") sort_by:String,
        @Query("page") page:Int,
        @Query("with_genres") genres:String
    ):Response<DiscoverResponse>

    @GET("search/{name}")
    suspend fun search(
        @Path("name") name:String,
        @Query("api_key") api_key: String =  API_KEY,
        @Query("query") query: String,
        @Query("page") page:Int):Response<DiscoverResponse>

    @GET("{name}/{id}")
    suspend fun getDetails(@Path("name") searchName:String,
                           @Path("id") id:Int,
                           @Query("api_key") api_key:String= API_KEY,
                           @Query("language") language:String ):Response<DetailResponse>

    @GET("{name}/{id}/images")
    suspend fun getImages(@Path("name") name:String,
                          @Path("id") id:Int,
                          @Query("api_key") api_key: String = API_KEY):Response<ImageResponse>

    @GET("videos")
    suspend fun getYoutubeVideoDetails(
        @Query("part")part:String,
        @Query("id")id:String,
        @Query("key")key:String =  YOUTUBE_API_KEY): Response<YoutubeResponse>


}
