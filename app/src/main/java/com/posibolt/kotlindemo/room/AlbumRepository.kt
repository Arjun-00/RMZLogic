import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.posibolt.kotlindemo.model.AlbumItem
import com.posibolt.kotlindemo.retrofilt.AlbumService
import com.posibolt.kotlindemo.room.AlbumDao
import com.posibolt.kotlindemo.room.AlbumEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class AlbumRepository(
    private val albumService: AlbumService,
    private val albumDao: AlbumDao) {

    suspend fun fetchAndSaveAlbums() {
        val response = albumService.getAlbums()
        if (response.isSuccessful) {
            val albums = response.body() ?: emptyList()
            val albumEntities = albums.map {
                AlbumEntity(it.id, it.title, it.userId, it.time,"",0)
            }
            albumDao.insertAlbums(albumEntities)
        }
    }

    fun getAlbumsBeforeOrEqualToTime(currentTime: Long): LiveData<List<AlbumItem>> {
        return albumDao.getAlbumsBeforeOrEqualToTime(currentTime)
    }
}

class AlbumViewModel(private val repository: AlbumRepository) : ViewModel() {
    fun fetchAndSaveAlbums() {
        viewModelScope.launch {
            repository.fetchAndSaveAlbums()
        }
    }
    fun getAlbumsBeforeOrEqualToTime(currentTime: Long): LiveData<List<AlbumItem>> {
        return repository.getAlbumsBeforeOrEqualToTime(currentTime)
    }

    fun observeAlbumsBeforeOrEqualToTimeContinuously(currentTime: Long): Flow<LiveData<List<AlbumItem>>> {
        return flow {
            while (true) {
                val albums = repository.getAlbumsBeforeOrEqualToTime(currentTime)
                emit(albums)
                delay(1000) // Delay for 1 second before emitting the next value
            }
        }.flowOn(Dispatchers.IO)
    }
}

class AlbumViewModelFactory(private val repository: AlbumRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlbumViewModel::class.java)) {
            return AlbumViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
