package com.drawiin.mymovielist.common.movie.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.drawiin.mymovielist.common.movie.data.entity.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class MovieDataBase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
