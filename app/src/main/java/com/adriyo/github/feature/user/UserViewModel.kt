package com.adriyo.github.feature.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adriyo.github.data.DispatcherProvider
import com.adriyo.github.data.model.User
import com.adriyo.github.data.repo.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by adriyo on 07/01/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: IUserRepository,
    private val dispatcher: DispatcherProvider,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun refresh() {
        viewModelScope.launch(dispatcher.main) {
            _uiState.update { it.copy(isLoading = true) }
            repository.getUsers()
                .flowOn(dispatcher.io)
                .catch { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            message = exception.message ?: "Error"
                        )
                    }
                }
                .collect { users ->
                    _uiState.update {
                        it.copy(isLoading = false, users = users)
                    }
                }
        }
    }

    fun onSelectUser(user: User) {
        _uiState.update { it.copy(selectedUser = user) }
    }

    fun clearSelectedUser() {
        _uiState.update { it.copy(selectedUser = null) }
    }

    fun onSearchInputChange(input: String) {
        _uiState.update { it.copy(searchInput = input) }
    }

    fun onOpenWebView(b: Boolean) {
        _uiState.update { it.copy(openWebView = b) }
    }

    fun onNavigateBack() {
        val state = uiState.value
        if (state.openWebView) {
            onOpenWebView(false)
        } else if (state.selectedUser != null) {
            clearSelectedUser()
        }
    }
}

data class UiState(
    val isLoading: Boolean = true,
    val users: List<User> = listOf(),
    val selectedUser: User? = null,
    val openWebView: Boolean = false,
    val message: String? = null,
    val searchInput: String = "",
)