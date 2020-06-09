package com.restodine.login.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MoviesList {
    @SerializedName("Search")
    @Expose
    var search: List<Movie>? = null
    @SerializedName("totalResults")
    @Expose
    var totalResults: String? = null
    @SerializedName("Response")
    @Expose
    var response: String? = null

}