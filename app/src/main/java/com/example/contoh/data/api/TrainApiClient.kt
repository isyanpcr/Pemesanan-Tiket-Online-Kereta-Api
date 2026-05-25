package com.example.contoh.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TrainApiClient {
    private const val BASE_URL = "https://neqbkthtpcyzfdysvonb.supabase.co/rest/v1/"
    private const val PUBLISHABLE_KEY = "sb_publishable_6tgOY1pTag4IiIvum1Ljxg_BDChCwq1" // use anon key on client

    val apiService: TrainApiService by lazy {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

        val authInterceptor = Interceptor { chain ->
            val req = chain.request().newBuilder()
                .addHeader("apikey", PUBLISHABLE_KEY)
                .addHeader("Authorization", "Bearer $PUBLISHABLE_KEY")
                .addHeader("Accept", "application/json")
                .build()
            chain.proceed(req)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(TrainApiService::class.java)
    }
}
