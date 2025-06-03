package com.drawiin.mymovielist.common.movie.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.drawiin.mymovielist.common.movie.data.entity.MovieEntity

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieEntity)

    @Query("SELECT * FROM movie WHERE id = :id")
    suspend fun getById(id: Int): MovieEntity?

    @Query("SELECT * FROM movie")
    suspend fun getAll(): List<MovieEntity>

    @Query("DELETE FROM movie WHERE id = :movieId")
    suspend fun delete(movieId: Int)
}
