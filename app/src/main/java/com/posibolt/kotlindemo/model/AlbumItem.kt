package com.posibolt.kotlindemo.model
import com.google.gson.annotations.SerializedName

data class AlbumItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("time")
    val time: Long,
    @SerializedName("type")
    val type: String,
    @SerializedName("occurrence")
    val occurrence: Int,
)