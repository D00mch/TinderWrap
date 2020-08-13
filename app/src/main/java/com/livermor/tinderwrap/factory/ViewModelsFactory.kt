package com.livermor.tinderwrap.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.livermor.tinderwrap.TinderApp
import com.livermor.tinderwrap.data.ApiFactory
import com.livermor.tinderwrap.data.AppDb
import com.livermor.tinderwrap.data.BioRepository
import com.livermor.tinderwrap.data.PhotoRepository
import com.livermor.tinderwrap.ui.screen.FixingType
import com.livermor.tinderwrap.ui.screen.fix.FixViewModel
import com.livermor.tinderwrap.ui.screen.swap.SwipeViewModel

object ViewModelsFactory {

    val api by lazy { ApiFactory(AppDb.token).get() }
    val photoRepo by lazy { PhotoRepository(TinderApp.instance, NamesImpl) }
    val bioRepo by lazy { BioRepository(TinderApp.instance, NamesImpl) }

    fun swapModel(storeOwner: ViewModelStoreOwner): SwipeViewModel {
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                require(modelClass.isAssignableFrom(SwipeViewModel::class.java))
                return SwipeViewModel(api, photoRepo, bioRepo) as T
            }
        }
        return ViewModelProvider(storeOwner, factory).get(SwipeViewModel::class.java)
    }

    fun fixModel(storeOwner: ViewModelStoreOwner, type: FixingType): FixViewModel {
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                require(modelClass.isAssignableFrom(FixViewModel::class.java))
                val photoRepo = PhotoRepository(TinderApp.instance, NamesImpl)
                return FixViewModel(photoRepo, type) as T
            }
        }
        return ViewModelProvider(storeOwner, factory).get(FixViewModel::class.java)
    }
}