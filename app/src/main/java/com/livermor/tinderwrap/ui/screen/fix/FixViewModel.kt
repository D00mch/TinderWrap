package com.livermor.tinderwrap.ui.screen.fix

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.livermor.tinderwrap.Estimated
import com.livermor.tinderwrap.UiPhoto
import com.livermor.tinderwrap.data.AppDb
import com.livermor.tinderwrap.data.PhotoRepository
import com.livermor.tinderwrap.factory.swipe
import com.livermor.tinderwrap.ui.screen.FixingType
import com.livermor.tinderwrap.ui.screen.SwapMessage
import com.livermor.tinderwrap.ui.viewmodel.BaseViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

class FixViewModel(
    private val photoRepository: PhotoRepository,
    private val fixingType: FixingType
) : BaseViewModel() {

    private val count = AppDb.fixSize

    val feed = MutableLiveData<PersistentList<UiPhoto>>()
    val end = MutableLiveData<Boolean>()
    val next = MutableLiveData<Unit>()

    init {
        withProgress { requestNextPhotos() }
    }

    fun update(msg: SwapMessage): Unit {
        return when (msg) {
            is SwapMessage.Swipe -> {
                val photos = feed.value!!
                val updatedPhoto = photos[msg.position].swipe(isLeft = msg.isLeft)
                if (fixingType != FixingType.ALL) next.value = Unit
                feed.value = photos.set(msg.position, updatedPhoto as UiPhoto)
            }
            is SwapMessage.Next -> viewModelScope.launch {
                withProgress {
                    moveCurrentPhotosToChecked()
                    requestNextPhotos()
                }
            }.let { Unit }
            is SwapMessage.Choose -> error("can't handle $msg")
        }
    }

    @WorkerThread
    private fun moveCurrentPhotosToChecked() {
        photoRepository.movePhotosToChecked(feed.value!!)
    }

    @WorkerThread
    private fun requestNextPhotos() {
        val photos = photoRepository.getPhotos(count, filterType(fixingType))
        if (photos.isEmpty()) {
            end.postValue(true)
            feed.postValue(persistentListOf())
        } else {
            feed.postValue(photos.toPersistentList())
        }
    }

    private fun filterType(type: FixingType): (UiPhoto) -> Boolean = { uiPhoto ->
        when (fixingType) {
            FixingType.GOOD -> uiPhoto.type == Estimated.Type.GOOD
            FixingType.BAD -> uiPhoto.type == Estimated.Type.BAD
            else -> true
        }
    }
}