package com.restodine.login.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(tableName = "Movie")
data class Movie (
    @PrimaryKey
    @SerializedName("Title")
    @Expose
    var title: String,
    @SerializedName("Year")
    @Expose
    var year: String? = null,
    @SerializedName("imdbID")
    @Expose
    var imdbID: String? = null,
    @SerializedName("Type")
    @Expose
    var type: String? = null,
    @SerializedName("Poster")
    @Expose
    var poster: String? = null
)