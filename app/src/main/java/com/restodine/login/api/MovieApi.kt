package com.restodine.login.api

import androidx.lifecycle.LiveData
import com.google.gson.JsonObject
import com.restodine.common.network.ApiResponse
import com.restodine.login.models.MovieDetail
import com.restodine.login.models.MoviesList
import retrofit2.http.*

interface MovieApi {



    @GET("/Purchase4/GetingLrDataBasedStratClosedTrip")
    fun getPurchaseList(
        @Query("BranchId") strBranchId: String, @Query("FlagSlNo") strFlagNo: String, @Query(
            "InputType"
        ) inputType: String
    ): LiveData<ApiResponse<JsonObject>>

    @GET("/")
    fun getSearchList(
        @Query("type") strType: String, @Query("apikey") strApiKey: String, @Query(
            "page"
        ) pageNum: Int, @Query("s") strSearchText: String
    ): LiveData<ApiResponse<MoviesList>>

    @GET("/")
    fun getMoviesList(
        @Query("type") strType: String, @Query("apikey") strApiKey: String,@Query("s") strSearchText: String
    ): LiveData<ApiResponse<MoviesList>>

    @GET("/")
    fun getMovieDetail(
        @Query("i") strType: String, @Query("apikey") strApiKey: String
    ): LiveData<ApiResponse<MovieDetail>>
}