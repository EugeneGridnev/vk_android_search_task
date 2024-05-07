package com.vk.usersapp

import com.vk.usersapp.feature.feed.api.UsersApi
import com.vk.usersapp.feature.feed.api.UsersRepository
import com.vk.usersapp.feature.feed.model.User
import com.vk.usersapp.feature.feed.model.UsersResponse
import kotlinx.coroutines.runBlocking
import org.junit.After

import org.junit.Assert.assertArrayEquals
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class UsersRepositoryTest {

    private val usersApi: UsersApi = mock()
    @After
    fun resetMocks() {
        Mockito.reset(usersApi)
    }
    @Test
    fun `WHEN get users EXPECT success with users response`() {

        val usersRepository = UsersRepository(usersApi)
        val expectedUsers = listOf(
            User(
                firstName = "Terry",
                lastName = "Medhurst",
                image = "https://robohash.org/Terry.png?set=set4",
                university = "Capitol University",
                age = 50
            )
        )
        val expectedResponse = UsersResponse(
            users = expectedUsers,
            total = 1,
            skip = 0,
            limit = 0
        )

        runBlocking {

            whenever(usersApi.getUsers(0, 0)).thenReturn(expectedResponse)

            val result = usersRepository.getUsers()

            assertArrayEquals(expectedUsers.toTypedArray(), result.toTypedArray())
        }
    }

    @Test
    fun `WHEN search users EXPECT success with users response`() {

        val usersRepository = UsersRepository(usersApi)
        val expectedUsers = listOf(
            User(
                firstName = "Terry",
                lastName = "Medhurst",
                image = "https://robohash.org/Terry.png?set=set4",
                university = "Capitol University",
                age = 50
            )
        )
        val expectedResponse = UsersResponse(
            users = expectedUsers,
            total = 1,
            skip = 0,
            limit = 0
        )

        runBlocking {

            whenever(usersApi.searchUsers("terry",0, 0 )).thenReturn(expectedResponse)

            val result = usersRepository.searchUsers("terry")

            assertArrayEquals(expectedUsers.toTypedArray(), result.toTypedArray())
        }
    }
}