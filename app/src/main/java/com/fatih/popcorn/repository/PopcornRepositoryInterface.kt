package com.fatih.popcorn.repository

import androidx.lifecycle.LiveData
import com.fatih.popcorn.entities.local.RoomEntity
import com.fatih.popcorn.entities.remote.creditsresponse.CreditsResponse
import com.fatih.popcorn.entities.remote.detailresponse.DetailResponse
import com.fatih.popcorn.entities.remote.discoverresponse.DiscoverResponse
import com.fatih.popcorn.entities.remote.imageresponse.ImageResponse
import com.fatih.popcorn.entities.remote.reviewresponse.ReviewResponse
import com.fatih.popcorn.entities.remote.videoresponse.VideoResponse
import com.fatih.popcorn.entities.remote.youtuberesponse.YoutubeResponse
import com.fatih.popcorn.other.Resource
import kotlinx.coroutines.flow.Flow

interface PopcornRepositoryInterface {

    suspend fun getMovies(sort_by:String, page:Int, genres:String):Resource<DiscoverResponse>
    suspend fun getTvShows(sort_by:String, page:Int, genres:String):Resource<DiscoverResponse>
    suspend fun search(name:String,query: String,page:Int):Resource<DiscoverResponse>
    fun getDetails(name:String,id:Int,language:String): Flow<Resource<DetailResponse>>
    suspend fun getImages( name:String, id:Int ): Flow<Resource<ImageResponse>>
    suspend fun insertRoomEntity(roomEntity: RoomEntity)
    suspend fun deleteRoomEntity(roomEntity: RoomEntity)
    fun getAllRoomEntity():LiveData<List<RoomEntity>>
    suspend fun getCredits(name: String, id: Int):Resource<CreditsResponse>
    suspend fun getSelectedRoomEntity(idInput:Int):Boolean
    suspend fun getReviews(name:String, id:Int, page:Int):Resource<ReviewResponse>
    suspend fun getRecommendations(name:String,id:Int,page:Int):Resource<DiscoverResponse>
    suspend fun getFamiliars(name:String,id:Int,page:Int):Resource<DiscoverResponse>
    suspend fun getVideos(name:String, id:Int):Resource<VideoResponse>
    suspend fun getYoutubeVideoDetails(part:String,id:String):Resource<YoutubeResponse>

}