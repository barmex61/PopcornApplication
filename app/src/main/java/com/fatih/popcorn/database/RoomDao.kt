package com.fatih.popcorn.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.fatih.popcorn.entities.local.RoomEntity

@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertRoomEntity(roomEntity: RoomEntity)
    @Query("DELETE FROM RoomEntity WHERE field_id = :idInput")
    suspend fun deleteRoomEntity(idInput: Int)
    @Query("SELECT * FROM RoomEntity")
    fun getAllRoomEntity(): LiveData<List<RoomEntity>>
    @Query("SELECT * FROM RoomEntity WHERE field_id = :idInput")
    suspend fun getSelectedRoomEntity(idInput:Int):RoomEntity?
    @Query("UPDATE RoomEntity SET favorite = :favoriteValue WHERE field_id = :idInput")
    suspend fun updateRoomEntityFavorite(idInput: Int, favoriteValue: Boolean)
}