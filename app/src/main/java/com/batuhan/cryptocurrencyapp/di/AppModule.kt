package com.batuhan.cryptocurrencyapp.di

import com.prof18.rssparser.RssParser
import com.prof18.rssparser.RssParserBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    @Provides
    @Singleton
    fun provideRSSParser(): RssParser {
        return RssParserBuilder(
            charset = Charsets.UTF_8
        ).build()
    }
}
