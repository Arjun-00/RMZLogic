package com.posibolt.kotlindemo.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "albums")
data class AlbumEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val userId: Int,
    val time: Long,
    val type:String,
    val occurrence:Int
 )
