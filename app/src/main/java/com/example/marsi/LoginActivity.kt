package com.example.marsi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.marsi.data.StubAuthRepository
import com.example.marsi.model.UserRole

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(StubAuthRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etLogin = findViewById<EditText>(R.id.etLogin)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val login = etLogin.text.toString().trim()
            val password = etPassword.text.toString().trim()

            viewModel.performLogin(login, password)
        }
        viewModel.state.observe(this) { state ->
            when (state) {
                is LoginViewModel.LoginState.Loading -> {
                    btnLogin.isEnabled = false
                    // Можно добавить прогресс-бар
                }

                is LoginViewModel.LoginState.Success -> {
                    val intent = if (state.role == UserRole.DISPATCHER) {
                        Intent(this, EmptyDispatcherActivity::class.java)
                    } else {
                        Intent(this, MainActivity::class.java)
                    }
                    startActivity(intent)
                    finish()
                }

                is LoginViewModel.LoginState.Error -> {
                    btnLogin.isEnabled = true
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }
}