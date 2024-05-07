package com.vk.usersapp.di

import com.vk.usersapp.feature.feed.api.IUsersRepository
import com.vk.usersapp.feature.feed.api.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun providesUsersRepository(usersRepository: UsersRepository): IUsersRepository
}