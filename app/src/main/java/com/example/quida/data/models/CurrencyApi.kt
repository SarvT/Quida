package com.example.quida.data.models

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    @GET("/")
    suspend fun getRates(
        @Query("base") base:String
    ):Response<CurrencyResponse>
}