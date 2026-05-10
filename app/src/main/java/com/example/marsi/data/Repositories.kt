package com.example.marsi.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.marsi.model.*
import kotlinx.coroutines.delay

interface AuthRepository {
    suspend fun login(login: String, pass: String): Result<User>
}

class StubAuthRepository : AuthRepository {
    override suspend fun login(login: String, pass: String): Result<User> {
        delay(1000) // Имитируем запрос к серверу

        return when {
            // Тестовый Курьер
            login == "courier" && pass == "1234" -> {
                Result.success(User("u_1", login, UserRole.COURIER, "token_courier"))
            }
            // Тестовый Диспетчер
            login == "admin" && pass == "1234" -> {
                Result.success(User("u_2", login, UserRole.DISPATCHER, "token_admin"))
            }
            else -> {
                Result.failure(Exception("Неверный логин или пароль"))
            }
        }
    }
}

interface DispatcherRepository {
    fun getDispatcherData(): LiveData<Dispatcher>
    suspend fun saveSchedule(schedule: Map<String, String>)
}

class StubDispatcherRepository : DispatcherRepository {
    private val _data = MutableLiveData(Dispatcher("d_01", "Алексей Д.", "WH-01"))
    override fun getDispatcherData(): LiveData<Dispatcher> = _data

    override suspend fun saveSchedule(schedule: Map<String, String>) {
        delay(500)
        val current = _data.value ?: return
        _data.postValue(current.copy(shiftSchedule = schedule))
    }
}