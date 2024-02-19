package com.example.quida.di

import com.example.quida.data.models.CurrencyApi
import com.example.quida.main.DefaultMainRepository
import com.example.quida.main.MainRepository
import com.example.quida.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

//private const val BASE_URL = "https://exchange-rates.abstractapi.com/v1/"
private const val BASE_URL = "https://exchange-rates.abstractapi.com/v1/live/?api_key=3269137f12454c77929fe9b6c492ab78&base=USD"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCurrencyApi(): CurrencyApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CurrencyApi::class.java)

    @Singleton
    @Provides
    fun provideMainRepository(api: CurrencyApi):MainRepository=DefaultMainRepository(api)

    @Singleton
    @Provides
    fun provideDispatchers():DispatcherProvider=object :DispatcherProvider{
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfirmed: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }
}