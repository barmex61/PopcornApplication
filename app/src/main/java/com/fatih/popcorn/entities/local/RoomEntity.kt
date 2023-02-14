package com.fatih.popcorn.entities.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomEntity(
    val lastAirDate: String,
    val posterPath: String,
    val voteAverage: Double,
    val isTvShow:Boolean,
    @PrimaryKey (autoGenerate = false)
    var field_id: Int
)
