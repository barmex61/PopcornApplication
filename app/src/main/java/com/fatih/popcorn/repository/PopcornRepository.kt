package com.fatih.popcorn.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.fatih.popcorn.database.RoomDao
import com.fatih.popcorn.entities.local.RoomEntity
import com.fatih.popcorn.entities.remote.detailresponse.DetailResponse
import com.fatih.popcorn.entities.remote.discoverresponse.DiscoverResponse
import com.fatih.popcorn.entities.remote.imageresponse.ImageResponse
import com.fatih.popcorn.movieapi.PopcornApi
import com.fatih.popcorn.other.Resource
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class PopcornRepository (
    private val popcornApi: PopcornApi,
    private val roomDao:RoomDao)   :PopcornRepositoryInterface {

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

    @InternalCoroutinesApi
    override fun getDetails(name: String, id: Int, language: String): Flow<Resource<DetailResponse>> = flow{
       emit(Resource.loading(null))
       val resource= try {
            val result=popcornApi.getDetails(searchName = name,id = id,language = language)
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
        emit(resource)
    }

    override suspend fun getImages(name: String, id: Int): Flow<Resource<ImageResponse>> = flow{
        emit(Resource.loading(null))
        val resource= try {
            val result=popcornApi.getImages(name = name , id = id)
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
        emit(resource)
    }

    override suspend fun insertRoomEntity(roomEntity: RoomEntity) {
        try {
            roomDao.insertRoomEntity(roomEntity)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override suspend fun deleteRoomEntity(roomEntity: RoomEntity) {
        try {
            roomDao.deleteRoomEntity(roomEntity)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun getAllRoomEntity(): LiveData<List<RoomEntity>> {
        return roomDao.getAllRoomEntity()
    }

    override suspend fun getSelectedRoomEntity(idInput: Int): RoomEntity? {
       return try {
           delay(300)
           roomDao.getSelectedRoomEntity(idInput)
       }catch (e:Exception){
           println(e.message)
           null
       }
    }
}