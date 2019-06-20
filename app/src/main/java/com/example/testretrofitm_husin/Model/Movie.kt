package com.example.testretrofitm_husin.Model

import androidx.appcompat.widget.DialogTitle
import com.google.gson.annotations.SerializedName

object Movie {
    data class Movie (@SerializedName("original_title")
    val originalTitle: String?,@SerializedName("overview")
    val overvieview : String?,
                      @SerializedName("poster_path") val posterPath
    : String?)
}