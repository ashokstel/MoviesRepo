package com.restodine.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.restodine.common.network.*
import com.restodine.login.api.MovieApi
import com.restodine.login.dao.MoviesDao
import com.restodine.login.models.MoviesList
import com.restodine.login.models.MovieDetail
import javax.inject.Inject


class MovieViewModel @Inject constructor(
    private val movieApi: MovieApi,
    private val moviesDao: MoviesDao
) : ViewModel() {

    fun getMoviesList(searchText: String): LiveData<Resource<MoviesList>> {
        return object : NetworkOnlyResource<MoviesList>() {
            override fun createCall(): LiveData<ApiResponse<MoviesList>> {
                return movieApi.getMoviesList("movie", "b9bd48a6", searchText)
            }
        }.asLiveData()
    }

    fun getMovieDetails(id: String): LiveData<Resource<MovieDetail>> {
        return object : NetworkOnlyResource<MovieDetail>() {
            override fun createCall(): LiveData<ApiResponse<MovieDetail>> {
                return movieApi.getMovieDetail(id, "b9bd48a6")
            }
        }.asLiveData()
    }

}