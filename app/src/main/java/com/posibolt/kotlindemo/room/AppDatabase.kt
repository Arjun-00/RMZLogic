package com.posibolt.kotlindemo.room

import android.content.Context
import androidx.lifecycle.LiveDataScope
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.posibolt.kotlindemo.model.Album
import retrofit2.Response

@Database(entities = [AlbumEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "albums_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
