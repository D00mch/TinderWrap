package com.livermor.tinderwrap

import android.app.Application
import com.chibatching.kotpref.Kotpref

class TinderApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance_ = this
        Kotpref.init(this)
    }

    companion object {
        private lateinit var instance_: TinderApp
        val instance: TinderApp get() = instance_
    }
}