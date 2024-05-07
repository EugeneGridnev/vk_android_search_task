package com.vk.usersapp.di

import com.vk.usersapp.di.annotations.BackgroundDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @Provides
    @BackgroundDispatcher
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO
}