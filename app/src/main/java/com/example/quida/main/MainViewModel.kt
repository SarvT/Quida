package com.example.quida.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quida.data.models.ExchangeRates
import com.example.quida.util.DispatcherProvider
import com.example.quida.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dispatchers: DispatcherProvider,
): ViewModel() {
    sealed class CurrencyEvent {
        class Success(val resText:String):CurrencyEvent()
        class Error(val errText:String):CurrencyEvent()
        object Loading:CurrencyEvent()
        object Empty: CurrencyEvent()
    }

    private val _conversion= MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion: StateFlow<CurrencyEvent> = _conversion

    fun convert(
        amtStr:String,
        frCurr:String,
        toCurr:String
    ){
        val fromAmt = amtStr.toFloatOrNull()
        if (fromAmt==null){
            _conversion.value=CurrencyEvent.Error("Invalid Amount")
            return
        }

        viewModelScope.launch(dispatchers.io) {
            _conversion.value = CurrencyEvent.Loading
            when(val ratesResponse = repository.getRates(frCurr)){
                is Resource.Error->_conversion.value  = CurrencyEvent.Error(ratesResponse.message!!)
                is Resource.Success->{
                    val rates = ratesResponse.data!!.exchange_rates
                    val rate = getRateForCurrency(toCurr, rates)
                    if (rate==null){
                        _conversion.value = CurrencyEvent.Error("Error Occurred!")
                    } else {
                        val currencyConverted = round(fromAmt * rate * 100) / 100
                        _conversion.value = CurrencyEvent.Success(
                            "$fromAmt $frCurr = $currencyConverted $toCurr"
                        )
                    }

                }
            }
        }
    }

    private fun getRateForCurrency(currency: String, rates: ExchangeRates) = when (currency) {
        "CAD" -> rates.CAD
        "HKD" -> rates.HKD
        "ISK" -> rates.ISK
        "EUR" -> rates.EUR
        "PHP" -> rates.PHP
        "DKK" -> rates.DKK
        "HUF" -> rates.HUF
        "CZK" -> rates.CZK
        "AUD" -> rates.AUD
        "RON" -> rates.RON
        "SEK" -> rates.SEK
        "IDR" -> rates.IDR
        "INR" -> rates.INR
        "BRL" -> rates.BRL
        "RUB" -> rates.RUB
        "HRK" -> rates.HRK
        "JPY" -> rates.JPY
        "THB" -> rates.THB
        "CHF" -> rates.CHF
        "SGD" -> rates.SGD
        "PLN" -> rates.PLN
        "BGN" -> rates.BGN
        "CNY" -> rates.CNY
        "NOK" -> rates.NOK
        "NZD" -> rates.NZD
        "ZAR" -> rates.ZAR
        "MXN" -> rates.MXN
        "ILS" -> rates.ILS
        "GBP" -> rates.GBP
        "KRW" -> rates.KRW
//        "USD" -> rates.USD
        "MYR" -> rates.MYR
        else -> null
    }
}