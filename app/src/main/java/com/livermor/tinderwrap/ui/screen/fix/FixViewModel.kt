package com.livermor.tinderwrap.ui.screen.fix

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.livermor.tinderwrap.UiPhoto
import com.livermor.tinderwrap.data.PhotoRepository
import com.livermor.tinderwrap.factory.swipe
import com.livermor.tinderwrap.ui.screen.SwapMessage
import com.livermor.tinderwrap.ui.viewmodel.BaseViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

class FixViewModel(
    private val photoRepository: PhotoRepository
) : BaseViewModel() {

    private var position = AtomicInteger(0)
    private val count = 1
    val feed = MutableLiveData<PersistentList<UiPhoto>>()
    val end = MutableLiveData<Boolean>()

    init {
        withProgress { requestNextPhotos() }
    }

    fun update(msg: SwapMessage): Unit {
        return when (msg) {
            is SwapMessage.Swipe -> {
                val photos = feed.value!!
                val updatedPhoto = photos[msg.position].swipe(isLeft = msg.isLeft)
                feed.value = photos.set(msg.position, updatedPhoto as UiPhoto)
            }
            is SwapMessage.Next -> viewModelScope.launch {
                withProgress {
                    persistCurrentPhotos()
                    requestNextPhotos()
                }
            }.let { Unit }
            is SwapMessage.Choose -> error("can't handle $msg")
        }
    }

    @WorkerThread
    private fun persistCurrentPhotos() {
        photoRepository.movePhotosToChecked(feed.value!!)
    }

    @WorkerThread
    private fun requestNextPhotos() {
        val photos = photoRepository.getPhotos(position.get(), count)
        if (photos.isEmpty()) {
            end.postValue(true)
            feed.postValue(persistentListOf())
        } else {
            feed.postValue(photos.toPersistentList())
            position.set(position.get() + count)
        }
    }
}