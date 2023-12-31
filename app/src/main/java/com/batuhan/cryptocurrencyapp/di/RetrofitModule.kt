package com.batuhan.cryptocurrencyapp.di

import com.batuhan.cryptocurrencyapp.data.source.remote.CryptoCurrencyService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(ViewModelComponent::class)
object RetrofitModule {

    @Provides
    @ViewModelScoped
    fun provideClient(): OkHttpClient {
        return OkHttpClient.Builder().readTimeout(15, TimeUnit.SECONDS).build()
    }

    @Provides
    @ViewModelScoped
    fun provideRetrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://api.coincap.io/").build()
    }

    @Provides
    @ViewModelScoped
    fun provideService(retrofit: Retrofit): CryptoCurrencyService {
        return retrofit.create(CryptoCurrencyService::class.java)
    }
}
