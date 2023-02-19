package com.fatih.popcorn.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fatih.popcorn.entities.local.RoomEntity

@Database(entities = [RoomEntity::class], version = 1, exportSchema = false)
abstract class RoomDb :RoomDatabase() {
    abstract fun roomDao():RoomDao
}