package com.fatih.popcorn.module

import android.content.Context
import android.view.animation.AnimationUtils
import androidx.room.Room
import com.fatih.popcorn.R
import com.fatih.popcorn.database.RoomDao
import com.fatih.popcorn.database.RoomDb
import com.fatih.popcorn.movieapi.PopcornApi
import com.fatih.popcorn.other.Constants.BASE_URL
import com.fatih.popcorn.other.Constants.YOUTUBE_BASE_URL
import com.fatih.popcorn.repository.PopcornRepository
import com.fatih.popcorn.repository.PopcornRepositoryInterface
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    @Named("PopcornApi")
    fun providePopcornApi()=Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
        .build().create(PopcornApi::class.java)

    @Provides
    @Singleton
    fun providePopcornRepo(@Named("PopcornApi")popcornApi: PopcornApi, roomDao: RoomDao, @Named("YoutubeApi") youtubeApi: PopcornApi)=PopcornRepository(popcornApi = popcornApi,roomDao, youtubeApi = youtubeApi) as PopcornRepositoryInterface

    @Provides
    @Singleton
    fun provideAnimation(@ApplicationContext context:Context)=AnimationUtils.loadAnimation(context,
        R.anim.fade_scale_animation)

    @Provides
    @Singleton
    fun provideRoomDao(@ApplicationContext context: Context)= Room.databaseBuilder(context,RoomDb::class.java,"RoomDatabase")
        .build().roomDao()

    @Provides
    @Named("YoutubeApi")
    @Singleton
    fun provideYoutubeApi()=Retrofit.Builder().baseUrl(YOUTUBE_BASE_URL).addConverterFactory(GsonConverterFactory.create())
        .build().create(PopcornApi::class.java)

}