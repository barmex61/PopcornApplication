package com.fatih.popcorn.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.fatih.popcorn.entities.local.RoomEntity

@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addTvShow(roomEntity: RoomEntity)
    @Delete
    suspend fun deleteTvShow(roomEntity: RoomEntity)
    @Query("SELECT * FROM RoomEntity")
    fun getAllTvShow(): LiveData<List<RoomEntity>>

}