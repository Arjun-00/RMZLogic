package com.posibolt.kotlindemo.retrofilt
import com.posibolt.kotlindemo.model.Album
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface AlbumService {
    companion object {
        fun create(): AlbumService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com") // Your base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(AlbumService::class.java)
        }
    }

    @GET("/albums")
    suspend fun getAlbums(): Response<Album>

}