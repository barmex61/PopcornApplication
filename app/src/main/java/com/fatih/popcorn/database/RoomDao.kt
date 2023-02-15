package com.fatih.popcorn.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.fatih.popcorn.entities.local.RoomEntity

@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertRoomEntity(roomEntity: RoomEntity)
    @Delete
    suspend fun deleteRoomEntity(roomEntity: RoomEntity)
    @Query("SELECT * FROM RoomEntity")
    fun getAllRoomEntity(): LiveData<List<RoomEntity>>

    @Query("SELECT * FROM RoomEntity WHERE field_id = :idInput")
    fun getSelectedRoomEntity(idInput:Int):RoomEntity

}