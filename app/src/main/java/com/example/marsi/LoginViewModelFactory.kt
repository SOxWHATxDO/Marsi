package com.example.marsi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.marsi.data.AuthRepository

class LoginViewModelFactory(private val authRepo: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // Передаем authRepo в конструктор LoginViewModel
            return LoginViewModel(authRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}