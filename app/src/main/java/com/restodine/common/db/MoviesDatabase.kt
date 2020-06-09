package com.restodine.common.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.restodine.App
import com.restodine.login.dao.MoviesDao
import com.restodine.login.models.Movie

@Database(entities = [(Movie::class)], version = 1, exportSchema = false)
abstract class MoviesDatabase : RoomDatabase() {
    abstract val moviesDao: MoviesDao
    companion object {
        val instance: MoviesDatabase by lazy {
            Room.databaseBuilder(App.app, MoviesDatabase::class.java, "movies.db")
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }
}