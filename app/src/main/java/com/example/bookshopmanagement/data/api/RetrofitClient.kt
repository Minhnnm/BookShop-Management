package com.example.bookshopmanagement.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://037a-116-99-34-22.ngrok-free.app/"

    private val retrofit: Retrofit by lazy {
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES) // Đặt thời gian kết nối tối đa
            .readTimeout(5, TimeUnit.MINUTES)    // Đặt thời gian đọc dữ liệu tối đa
            .writeTimeout(5, TimeUnit.MINUTES)   // Đặt thời gian ghi dữ liệu tối đa

        val client = httpClient.build()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val apiService: ApiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }
}