package com.example.marsi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.marsi.data.CourierRepository

class MainViewModelFactory(private val repo: CourierRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}