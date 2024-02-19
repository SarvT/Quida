package com.example.quida.main

import com.example.quida.data.models.CurrencyResponse
import com.example.quida.util.Resource

interface MainRepository {

    suspend fun getRates(base:String):Resource<CurrencyResponse>
}