package com.example.weatherapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface LoggerApiService {
    @POST("message")
    suspend fun sendLog (
        @Body body: LoggerRequest
    ): LoggerResponse
}

private const val BASE_URL = "http://10.0.2.2:3000/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

object LoggerApi {
    val retrofitService: LoggerApiService by lazy {
        retrofit.create(LoggerApiService::class.java)
    }
}