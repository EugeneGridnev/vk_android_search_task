package com.vk.usersapp.feature.feed.api

import com.vk.usersapp.core.Retrofit
import com.vk.usersapp.feature.feed.model.User

interface IUsersRepository {
    suspend fun getUsers(): List<User>

    suspend fun searchUsers(query: String): List<User>
}

class UsersRepository(userApi: UsersApi? = null) : IUsersRepository {
    private val api: UsersApi by lazy {
        userApi ?: Retrofit.getClient().create(UsersApi::class.java)
    }

    override suspend fun getUsers(): List<User> {
        return api.getUsers(
            limit = 0,
            skip = 0
        ).users
    }

    override suspend fun searchUsers(query: String): List<User> {
        return api.searchUsers(
            query = query,
            limit = 0,
            skip = 0
        ).users
    }
}