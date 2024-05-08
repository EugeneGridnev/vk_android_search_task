package com.vk.usersapp.di

import com.vk.usersapp.feature.feed.api.UsersApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
internal object ApiModule {

    private const val BASE_URL = "https://dummyjson.com"
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideUserApi(retrofit: Retrofit): UsersApi = retrofit.create(UsersApi::class.java)
}