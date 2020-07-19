package com.livermor.tinderwrap

import android.app.Application
import com.chibatching.kotpref.Kotpref

class TinderApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Kotpref.init(this)
    }
}