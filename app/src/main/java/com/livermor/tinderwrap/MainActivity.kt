package com.livermor.tinderwrap

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tinderApi = ServiceBuilder.buildService(TinderApi::class.java)
        GlobalScope.launch {
            val response: Response = tinderApi.getUsers()
            val userObject = response.data.results.first()
            Log.i(MainActivity::class.java.simpleName, "first $userObject ")
        }
    }
}

