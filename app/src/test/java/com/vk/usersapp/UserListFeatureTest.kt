package com.vk.usersapp

import com.vk.usersapp.TestRules.MainDispatcherRule
import com.vk.usersapp.feature.feed.api.IUsersRepository
import com.vk.usersapp.feature.feed.model.User
import com.vk.usersapp.feature.feed.presentation.UserListAction
import com.vk.usersapp.feature.feed.presentation.UserListFeature
import com.vk.usersapp.feature.feed.presentation.UserListReducer
import com.vk.usersapp.feature.feed.presentation.UserListViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class UserListFeatureTest {

    private val usersRepository: IUsersRepository = mock()

    private val reducer = UserListReducer()

    private val expectedUsers = listOf(
        User(
            firstName = "Terry",
            lastName = "Medhurst",
            image = "https://robohash.org/Terry.png?set=set4",
            university = "Capitol University",
            age = 50
        )
    )

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @After
    fun resetMocks() {
        Mockito.reset(usersRepository)
    }


    @Test
    fun `WHEN init action EXPECT loaded list`() = runTest {
        val userListFeature = UserListFeature(
            usersRepository = usersRepository,
            reducer = reducer,
            backgroundDispatcher = Dispatchers.Main
        )

        whenever(usersRepository.getUsers()).thenReturn(expectedUsers)

        userListFeature.submitAction(UserListAction.Init)

        val viewStateLoading = userListFeature.viewStateFlow.value

        assertTrue(viewStateLoading is UserListViewState.Loading)

        advanceUntilIdle()

        val viewStateList = userListFeature.viewStateFlow.value

        assertTrue(viewStateList is UserListViewState.List)

        assertArrayEquals(expectedUsers.toTypedArray(), (viewStateList as UserListViewState.List).itemsList.toTypedArray())

    }

    @Test
    fun `WHEN usersLoaded action EXPECT loaded list`() = runTest {
        val userListFeature = UserListFeature(
            usersRepository = usersRepository,
            reducer = reducer,
            backgroundDispatcher = Dispatchers.Main
        )

        userListFeature.submitAction(UserListAction.UsersLoaded(expectedUsers))

        val viewStateList = userListFeature.viewStateFlow.value

        assertTrue(viewStateList is UserListViewState.List)

        assertArrayEquals(expectedUsers.toTypedArray(), (viewStateList as UserListViewState.List).itemsList.toTypedArray())

        advanceUntilIdle()

        assertEquals(viewStateList, userListFeature.viewStateFlow.value)

    }

    @Test
    fun `WHEN empty query changed action EXPECT loaded list without query match`() = runTest {
        val userListFeature = UserListFeature(
            usersRepository = usersRepository,
            reducer = reducer,
            backgroundDispatcher = Dispatchers.Main
        )

        whenever(usersRepository.getUsers()).thenReturn(expectedUsers)

        userListFeature.submitAction(UserListAction.QueryChanged(""))

        val viewStateLoading = userListFeature.viewStateFlow.value

        assertTrue(viewStateLoading is UserListViewState.Loading)

        advanceUntilIdle()

        val viewStateList = userListFeature.viewStateFlow.value

        assertTrue(viewStateList is UserListViewState.List)

        assertArrayEquals(expectedUsers.toTypedArray(), (viewStateList as UserListViewState.List).itemsList.toTypedArray())

    }

    @Test
    fun `WHEN not empty query changed action EXPECT loaded list with query match`() = runTest {
        val query = "terry"

        val userListFeature = UserListFeature(
            usersRepository = usersRepository,
            reducer = reducer,
            backgroundDispatcher = Dispatchers.Main
        )

        whenever(usersRepository.searchUsers(query)).thenReturn(expectedUsers)

        userListFeature.submitAction(UserListAction.QueryChanged(query))

        val viewStateLoading = userListFeature.viewStateFlow.value

        assertTrue(viewStateLoading is UserListViewState.Loading)

        advanceUntilIdle()

        val viewStateList = userListFeature.viewStateFlow.value

        assertTrue(viewStateList is UserListViewState.List)

        assertArrayEquals(expectedUsers.toTypedArray(), (viewStateList as UserListViewState.List).itemsList.toTypedArray())

    }

    @Test
    fun `WHEN load error action EXPECT error state`() = runTest {

        val userListFeature = UserListFeature(
            usersRepository = usersRepository,
            reducer = reducer,
            backgroundDispatcher = Dispatchers.Main
        )


        userListFeature.submitAction(UserListAction.LoadError("Some error"))

        val viewStateError = userListFeature.viewStateFlow.value

        assertTrue(viewStateError is UserListViewState.Error)

        advanceUntilIdle()

        assertEquals(viewStateError, userListFeature.viewStateFlow.value)

    }

    @Test
    fun `WHEN init action EXPECT error state`() = runTest {
        val errorMessage = "something is wrong"

        val userListFeature = UserListFeature(
            usersRepository = usersRepository,
            reducer = reducer,
            backgroundDispatcher = Dispatchers.Main
        )

        whenever(usersRepository.getUsers()).thenThrow(RuntimeException(errorMessage))

        userListFeature.submitAction(UserListAction.Init)

        val viewStateLoading = userListFeature.viewStateFlow.value

        assertTrue(viewStateLoading is UserListViewState.Loading)

        advanceUntilIdle()

        val viewStateList = userListFeature.viewStateFlow.value

        assertTrue(viewStateList is UserListViewState.Error)

        assertEquals(errorMessage, (viewStateList as UserListViewState.Error).errorText)

    }
}