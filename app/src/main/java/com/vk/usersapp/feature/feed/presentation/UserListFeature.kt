package com.vk.usersapp.feature.feed.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk.usersapp.core.MVIFeature
import com.vk.usersapp.di.annotations.BackgroundDispatcher
import com.vk.usersapp.feature.feed.api.IUsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

// MVI:
//         Action                 patch, state                  newState                            viewState
// View ------------> Feature -----------------> reducer ------------------------> Feature --------------------------> view
//          ^            |
//          |            |
//          | Action     |  sideEffect
//          |            |
//          |            v
//          |-------- Feature
@HiltViewModel
class UserListFeature @Inject constructor(
    private val reducer: UserListReducer,
    private val usersRepository: IUsersRepository,
    @BackgroundDispatcher
    private val backgroundDispatcher: CoroutineDispatcher
) : MVIFeature, ViewModel() {
    private val mutableViewStateFlow =
        MutableStateFlow<UserListViewState>(UserListViewState.Loading)
    val viewStateFlow: StateFlow<UserListViewState> = mutableViewStateFlow.asStateFlow()

    private var state: UserListState = UserListState()

    fun submitAction(action: UserListAction) {
        state = reducer.applyAction(action, state)

        val viewState = createViewState(state)
        mutableViewStateFlow.value = viewState

        when (action) {
            UserListAction.Init,
            is UserListAction.QueryChanged -> submitSideEffect(UserListSideEffect.LoadUsers(state.query))
            is UserListAction.UsersLoaded,
            is UserListAction.LoadError -> Unit
        }
    }

    private fun createViewState(state: UserListState): UserListViewState {
        return when {
            state.isLoading -> UserListViewState.Loading
            !state.error.isNullOrBlank() -> UserListViewState.Error(state.error)
            else -> UserListViewState.List(state.items)
        }
    }

    private fun submitSideEffect(sideEffect: UserListSideEffect) {
        when (sideEffect) {
            is UserListSideEffect.LoadUsers -> loadUsers(sideEffect.query)
        }
    }

    private fun loadUsers(query: String) {
        viewModelScope.launch {
            try {
                val users = withContext(backgroundDispatcher) {
                    if (query.isBlank()) {
                        usersRepository.getUsers()
                    } else {
                        usersRepository.searchUsers(query)
                    }
                }
                submitAction(UserListAction.UsersLoaded(users))
            } catch (e: Exception) {
                Log.e("DIMAA", e.toString())
                submitAction(UserListAction.LoadError(e.message ?: "FATAL"))
            }
        }
    }
}