package com.example.contoh.data.api

import com.example.contoh.data.model.Ticket
import retrofit2.http.GET
import retrofit2.http.Query

interface TrainApiService {
    @GET("tiket_kereta_online")
    suspend fun getTickets(
        @Query("select") select: String = "*"
    ): List<Ticket>
}