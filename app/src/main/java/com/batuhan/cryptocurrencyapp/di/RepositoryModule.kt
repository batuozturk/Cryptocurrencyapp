package com.batuhan.cryptocurrencyapp.di

import com.batuhan.cryptocurrencyapp.data.repository.CryptoCurrencyRepository
import com.batuhan.cryptocurrencyapp.data.repository.CryptoCurrencyRepositoryImpl
import com.batuhan.cryptocurrencyapp.data.source.local.CryptoCurrencyLocalDataSource
import com.batuhan.cryptocurrencyapp.data.source.remote.CryptoCurrencyDataSource
import com.prof18.rssparser.RssParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideRepository(
        localDataSource: CryptoCurrencyLocalDataSource,
        remoteDataSource: CryptoCurrencyDataSource,
        rssParser: RssParser
    ): CryptoCurrencyRepository {
        return CryptoCurrencyRepositoryImpl(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            rssParser = rssParser
        )
    }
}
