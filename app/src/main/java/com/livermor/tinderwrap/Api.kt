package com.livermor.tinderwrap

import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object ServiceBuilder {
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .header("X-Auth-Token", "c968a6c4-d840-45dc-82f5-7b584996c342")
                .build()
            chain.proceed(request)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.gotinder.com/v2/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }
}

interface TinderApi {
    @GET("recs/core")
    suspend fun getUsers(): Response
}
