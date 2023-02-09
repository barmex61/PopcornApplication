package com.fatih.popcorn.module

import com.fatih.popcorn.movieapi.PopcornApi
import com.fatih.popcorn.other.Constants.BASE_URL
import com.fatih.popcorn.repository.PopcornRepository
import com.fatih.popcorn.repository.PopcornRepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun providePopcornApi()=Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
        .build().create(PopcornApi::class.java)

    @Provides
    @Singleton
    fun providePopcornRepo(popcornApi: PopcornApi)=PopcornRepository(popcornApi) as PopcornRepositoryInterface

}