package com.restodine.login.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.restodine.login.models.Movie

@Dao
abstract class MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMoviesList(moviesList: List<Movie>)

    @Query("Select * from movie")
    abstract fun getMoviesList() : LiveData<List<Movie>>

    @Query("Select * from movie where imdbID=:id")
    abstract fun getMovieDetail(id:String) : Movie

    @Query("Delete from movie")
    abstract fun deleteAllMoviesList()
}