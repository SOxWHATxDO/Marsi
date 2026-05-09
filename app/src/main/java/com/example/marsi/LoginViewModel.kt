package com.example.marsi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    sealed class Navigation {
        object MainActivity : Navigation()
        object EmptyDispatcherActivity : Navigation()
        data class Error(val message: String) : Navigation()
    }

    private val _navigation = MutableLiveData<Navigation>()
    val navigation: LiveData<Navigation> = _navigation

    fun login(login: String, password: String, isDispatcher: Boolean) {
        if (login == "123" && password == "1234") {
            _navigation.value = if (isDispatcher) {
                Navigation.EmptyDispatcherActivity
            } else {
                Navigation.MainActivity
            }
        } else {
            _navigation.value = Navigation.Error("Неверный логин или пароль")
        }
    }
}