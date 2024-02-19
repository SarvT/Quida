package com.example.quida.data.models

data class CurrencyResponse(
    val base: String,
    val exchange_rates: ExchangeRates,
    val last_updated: Int
)