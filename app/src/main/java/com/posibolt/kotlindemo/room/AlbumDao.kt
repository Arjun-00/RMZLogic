package com.posibolt.kotlindemo.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.posibolt.kotlindemo.model.Album
import com.posibolt.kotlindemo.model.AlbumItem

@Dao
interface AlbumDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(albums: List<AlbumEntity>)

    @Query("UPDATE albums SET time = :newTime, type = :newType, occurrence = :occurrence WHERE id = :albumId")
    suspend fun updateTime(albumId: Int, newTime: Long,newType: String,occurrence :Int)

    @Query("SELECT * FROM albums")
    suspend fun getAllAlbums(): List<AlbumItem>

    @Query("SELECT * FROM albums WHERE time <= :currentTime")
    fun getAlbumsBeforeOrEqualToTime(currentTime: Long): LiveData<List<AlbumItem>>
}
