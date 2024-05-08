package com.vk.usersapp.feature.feed.api

import com.vk.usersapp.feature.feed.model.User
import javax.inject.Inject

interface IUsersRepository {
    suspend fun getUsers(): List<User>

    suspend fun searchUsers(query: String): List<User>
}

class UsersRepository @Inject constructor(private val api: UsersApi) : IUsersRepository {

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