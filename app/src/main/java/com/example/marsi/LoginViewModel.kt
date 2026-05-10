package com.example.marsi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marsi.data.AuthRepository
import com.example.marsi.model.UserRole
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepo: AuthRepository) : ViewModel() {

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        data class Success(val role: UserRole) : LoginState()
        data class Error(val message: String) : LoginState()
    }

    private val _state = MutableLiveData<LoginState>(LoginState.Idle)
    val state: LiveData<LoginState> = _state

    fun performLogin(login: String, pass: String) {
        if (login.isEmpty() || pass.isEmpty()) {
            _state.value = LoginState.Error("Заполните все поля")
            return
        }

        _state.value = LoginState.Loading
        viewModelScope.launch {
            authRepo.login(login, pass)
                .onSuccess { user ->
                    _state.value = LoginState.Success(user.role)
                }
                .onFailure {
                    _state.value = LoginState.Error("Неверные данные")
                }
        }
    }
}