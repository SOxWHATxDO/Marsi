package com.example.marsi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels { LoginViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etLogin = findViewById<EditText>(R.id.etLogin)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val rbDispatcher = findViewById<RadioButton>(R.id.rbDispatcher)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val login = etLogin.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val isDispatcher = rbDispatcher.isChecked
            viewModel.login(login, password, isDispatcher)
        }

        viewModel.navigation.observe(this) { event ->
            when (event) {
                is LoginViewModel.Navigation.MainActivity -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                is LoginViewModel.Navigation.EmptyDispatcherActivity -> {
                    startActivity(Intent(this, EmptyDispatcherActivity::class.java))
                    finish()
                }
                is LoginViewModel.Navigation.Error -> {
                    Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}