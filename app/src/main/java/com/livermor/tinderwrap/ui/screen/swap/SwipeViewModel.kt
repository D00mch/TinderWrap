package com.livermor.tinderwrap.ui.screen.swap

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.livermor.tinderwrap.Bio
import com.livermor.tinderwrap.Estimated
import com.livermor.tinderwrap.LikeResponse
import com.livermor.tinderwrap.UiPhoto
import com.livermor.tinderwrap.UiUser
import com.livermor.tinderwrap.data.BioRepository
import com.livermor.tinderwrap.data.PhotoRepository
import com.livermor.tinderwrap.data.TinderApi
import com.livermor.tinderwrap.factory.swipe
import com.livermor.tinderwrap.ui.screen.SwapMessage
import com.livermor.tinderwrap.ui.viewmodel.BaseViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.math.floor

class SwipeViewModel(
    private val api: TinderApi,
    private val photoRepository: PhotoRepository,
    private val bioRepository: BioRepository
) : BaseViewModel() {

    private var users = persistentListOf<UiUser>()
    val feed = MutableLiveData<PersistentList<Estimated>>()
    val noMoreAccounts = MutableLiveData<Boolean>()
    val errors = MutableLiveData<Exception>()
    val age = MutableLiveData<String>()

    init {
        viewModelScope.launch { postNextUser() }
    }

    fun update(msg: SwapMessage): Unit {
        val estimated = requireNotNull(feed.value)
        when (msg) {
            is SwapMessage.Choose -> viewModelScope.launch {
                withProgress {
                    val resp: LikeResponse = when {
                        msg.like -> api.like(users.first().id)
                        else -> api.hate(users.first().id)
                    }
                    Log.i(SwipeViewModel::class.java.simpleName, "update: response $resp")

                    photoRepository.saveCheckedPhotos(estimated.filterIsInstance(UiPhoto::class.java))
                    bioRepository.saveBio(estimated.first() as Bio)

                    users = users.removeAt(0)
                    postNextUser()
                }
            }
            is SwapMessage.Swipe -> {
                var currentPhotos = estimated
                val updatedPhoto = currentPhotos[msg.position].swipe(isLeft = msg.isLeft) // O(1)
                currentPhotos = currentPhotos.set(msg.position, updatedPhoto) // O(1)
                feed.value = currentPhotos
            }
            is SwapMessage.Next -> error("can't handle ${SwapMessage.Next}")
        }
    }

    private suspend fun postNextUser() {
        if (users.isEmpty()) requestUsers()
        if (users.isEmpty()) {
            noMoreAccounts.postValue(true)
        } else {
            users.first().let { user ->
                age.postValue(user.birthDate?.toAge() ?: "unknown")
                val items = (user.photos as PersistentList<Estimated>).add(0, Bio(user.bio))
                feed.postValue(items)
            }
        }
    }

    private suspend fun requestUsers() = try {
        users = api.getUsers().data.results.map { userObject ->
            userObject.user.run {
                val uiPhotos = photos.map { UiPhoto(id = it.id, url = it.url) }.toPersistentList()
                UiUser(_id, bio, photos = uiPhotos, birthDate = birth_date)
            }
        }.toPersistentList()
    } catch (e: Exception) {
        errors.postValue(e)
    }

    private fun Date.toAge(): String {
        val now = Date()
        val timeBetween = now.time - time
        val yearsBetween = timeBetween / 3.15576e+10
        return "age: ${floor(yearsBetween).toInt()}"
    }
}