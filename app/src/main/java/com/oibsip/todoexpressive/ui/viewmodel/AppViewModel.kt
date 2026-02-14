package com.oibsip.todoexpressive.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.oibsip.todoexpressive.data.model.TaskItem
import com.oibsip.todoexpressive.data.repo.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AuthUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val error: String? = null,
    val isLoading: Boolean = false
)

class AppViewModel(private val repository: AppRepository) : ViewModel() {
    private val _currentUserId = MutableStateFlow(repository.currentUserId())
    val currentUserId: StateFlow<Long?> = _currentUserId

    private val _authState = MutableStateFlow(AuthUiState())
    val authState: StateFlow<AuthUiState> = _authState

    val tasks = _currentUserId.flatMapLatest { userId ->
        if (userId == null) flowOf(emptyList()) else repository.tasks(userId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateName(value: String) = _authState.value.let { _authState.value = it.copy(fullName = value) }
    fun updateEmail(value: String) = _authState.value.let { _authState.value = it.copy(email = value) }
    fun updatePassword(value: String) = _authState.value.let { _authState.value = it.copy(password = value) }
    fun clearError() = _authState.value.let { _authState.value = it.copy(error = null) }

    fun signUp(onSuccess: () -> Unit) {
        val state = _authState.value
        if (state.fullName.isBlank() || state.email.isBlank() || state.password.length < 6) {
            _authState.value = state.copy(error = "Please fill all fields and use a 6+ char password")
            return
        }
        viewModelScope.launch {
            _authState.value = state.copy(isLoading = true, error = null)
            repository.signUp(state.fullName, state.email, state.password)
                .onSuccess {
                    _currentUserId.value = it
                    _authState.value = AuthUiState()
                    onSuccess()
                }
                .onFailure {
                    _authState.value = state.copy(error = it.message, isLoading = false)
                }
        }
    }

    fun login(onSuccess: () -> Unit) {
        val state = _authState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            _authState.value = state.copy(error = "Email and password are required")
            return
        }
        viewModelScope.launch {
            _authState.value = state.copy(isLoading = true, error = null)
            repository.login(state.email, state.password)
                .onSuccess {
                    _currentUserId.value = it
                    _authState.value = AuthUiState()
                    onSuccess()
                }
                .onFailure {
                    _authState.value = state.copy(error = it.message, isLoading = false)
                }
        }
    }

    fun logout(onDone: () -> Unit) {
        repository.logout()
        _currentUserId.value = null
        onDone()
    }

    fun addTask(title: String, details: String, category: String) {
        val ownerId = _currentUserId.value ?: return
        viewModelScope.launch {
            repository.addTask(ownerId, title, details, category)
        }
    }

    fun toggleTask(task: TaskItem) {
        viewModelScope.launch {
            repository.toggleTask(task)
        }
    }

    fun deleteTask(taskId: Long) {
        viewModelScope.launch {
            repository.deleteTask(taskId)
        }
    }

    class Factory(private val repository: AppRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AppViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
