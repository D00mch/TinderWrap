package com.livermor.tinderwrap.data

import androidx.core.graphics.drawable.TintAwareDrawable
import com.livermor.tinderwrap.LikeResponse
import com.livermor.tinderwrap.Response
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class ApiFactory(private val token: String) {
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .header("X-Auth-Token", token)
                .build()
            chain.proceed(request)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.gotinder.com/v2/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun get(): TinderApi {
        return retrofit.create(TinderApi::class.java)
    }
}

interface TinderApi {
    @GET("recs/core")
    suspend fun getUsers(): Response

    @GET("https://api.gotinder.com/like/{id}")
    suspend fun like(@Path("id") id: String): LikeResponse

    @GET("https://api.gotinder.com/pass/{id}")
    suspend fun hate(@Path("id") id: String): LikeResponse
}
