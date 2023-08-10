package com.posibolt.kotlindemo

import AlbumAdapter
import AlbumRepository
import AlbumViewModel
import AlbumViewModelFactory
import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.posibolt.kotlindemo.model.Album
import com.posibolt.kotlindemo.model.AlbumItem
import com.posibolt.kotlindemo.retrofilt.AlbumService
import com.posibolt.kotlindemo.retrofilt.RetrofitInstance
import com.posibolt.kotlindemo.room.AlbumDao
import com.posibolt.kotlindemo.room.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll
import okhttp3.internal.toImmutableList
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var albumDao: AlbumDao
    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var albumViewModel: AlbumViewModel
    private lateinit var albumService: AlbumService
    private lateinit var textViewTitle: TextView
    private lateinit var hardButton: Button
    private lateinit var mediumButton: Button
    private lateinit var easyButton: Button
    private var currentIndex = 0;
    private lateinit var albumItems: ArrayList<AlbumItem>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textViewTitle = findViewById(R.id.textTitle);
        hardButton = findViewById(R.id.hardButton);
        easyButton = findViewById(R.id.easyButton);
        mediumButton = findViewById(R.id.mediumButton);
        albumAdapter = AlbumAdapter(emptyList())
        albumDao = AppDatabase.getInstance(this).albumDao()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        albumService = retrofit.create(AlbumService::class.java)
        val albumRepository = AlbumRepository(albumService, albumDao)
        albumViewModel = ViewModelProvider(this, AlbumViewModelFactory(albumRepository))
            .get(AlbumViewModel::class.java)
        albumViewModel.fetchAndSaveAlbums()
        albumAdapter = AlbumAdapter(emptyList())
        albumViewModel.fetchAndSaveAlbums()
        lifecycleScope.launch {
            fetchAndDisplayData()
        }


    }

    private fun fetchAndDisplayData() {
        lifecycleScope.launch(Dispatchers.IO) {
            albumItems = albumDao.getAllAlbums() as ArrayList<AlbumItem> // Fetch data from Room
            runOnUiThread {
                if (albumItems.isNotEmpty()) {
                    textViewTitle.text = albumItems[0].title;
                    Log.i("MYTAG1",albumItems[0].title+"NO :"+albumItems[0].id);
                    mediumButton.setOnClickListener {
                        val currentTime = System.currentTimeMillis()
                        mediumButtonClick(albumItems,"Medium")
                        albumViewModel.getAlbumsBeforeOrEqualToTime(currentTime)
                            .observe(this@MainActivity, { albums ->
                                Log.i("MYTAG","LIVE DATA IS SHOWING......! ");
                                Log.i("MYTAG",albums.toString());
                                updateUI(albums)
                            })
                    }
                    hardButton.setOnClickListener {
                        val currentTime = System.currentTimeMillis()
                        hardButtonClick(albumItems,"Hard")
                        albumViewModel.getAlbumsBeforeOrEqualToTime(currentTime)
                            .observe(this@MainActivity, { albums ->
                                Log.i("MYTAG","LIVE DATA IS SHOWING......! ");
                                Log.i("MYTAG",albums.toString());
                                updateUI(albums)
                            })
                    }
                    easyButton.setOnClickListener {
                        val currentTime = System.currentTimeMillis()
                        easyButtonClick(albumItems,"Easy")
                        albumViewModel.getAlbumsBeforeOrEqualToTime(currentTime)
                            .observe(this@MainActivity, { albums ->
                                Log.i("MYTAG","LIVE DATA IS SHOWING......! ");
                                Log.i("MYTAG",albums.toString());
                                updateUI(albums)
                            })
                    }
                }
            }
        }
    }

    private fun mediumButtonClick(albumList : List<AlbumItem>,type:String){
        CoroutineScope(Dispatchers.IO).launch {
            val currentTime = System.currentTimeMillis()
            var occurance = albumList[currentIndex].occurrence+1;
            var timees = 25000 * occurance;
            albumDao.updateTime(albumList[currentIndex].id,currentTime+timees,type,occurance)
            currentIndex = currentIndex +1;
            displayAlbumItemsWithInterval(albumList[currentIndex])
        }
    }
    private fun hardButtonClick(albumList : List<AlbumItem>,type:String){
        CoroutineScope(Dispatchers.IO).launch {
            val currentTime = System.currentTimeMillis()
            var occurance = albumList[currentIndex].occurrence+1;
            var timees = 20000 * occurance;
            albumDao.updateTime(albumList[currentIndex].id,currentTime+timees,type,occurance)
            currentIndex = currentIndex +1;
            displayAlbumItemsWithInterval(albumList[currentIndex])
        }
    }
    private fun easyButtonClick(albumList : List<AlbumItem>,type:String){
        CoroutineScope(Dispatchers.IO).launch {
            val currentTime = System.currentTimeMillis()
            var occurance = albumList[currentIndex].occurrence+1;
            var timees = 10000 * occurance;
            val newTime = currentTime + 10000;
            albumDao.updateTime(albumList[currentIndex].id,currentTime+timees,type,occurance)
            currentIndex = currentIndex +1;
            displayAlbumItemsWithInterval(albumList[currentIndex])
        }
    }
    private fun displayAlbumItemsWithInterval(albumItems: AlbumItem) {
        lifecycleScope.launch {
                runOnUiThread {
                    textViewTitle.text = albumItems.title;
                }
            }
    }

    private fun updateUI(albums: List<AlbumItem>) {
        val stringBuilder = StringBuilder()
        if(albums !=null&& albums.size >0) {
            albumItems.addAll(currentIndex, albums)
        }
    }
}