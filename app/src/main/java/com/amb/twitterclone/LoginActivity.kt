package com.amb.twitterclone

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    private val button: Button by lazy { findViewById(R.id.buttonLogin) }
    private val loginProgressLayout: LinearLayout by lazy { findViewById(R.id.loginProgressLayout) }
    private val emailInputLayout: TextInputLayout by lazy { findViewById(R.id.emailTIL) }
    private val passwordInputLayout: TextInputLayout by lazy { findViewById(R.id.passwordTIL) }
    private val emailInput: TextInputEditText by lazy { findViewById(R.id.emailET) }
    private val passwordInput: TextInputEditText by lazy { findViewById(R.id.passwordET) }
    private val signupTV: TextView by lazy { findViewById(R.id.signupTV) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.loginStateLiveData.observe(this) { state ->
            when (state) {
                is LoginState.Success -> {
                    loginProgressLayout.visibility = View.GONE
                    //TODO continue flow
                }
                is LoginState.Failure -> {
                    loginProgressLayout.visibility = View.GONE
                    //TODO show error
                }
                is LoginState.Loading -> {
                    loginProgressLayout.visibility = View.VISIBLE
                }
                is LoginState.EmailError -> {
                    loginProgressLayout.visibility = View.GONE
                    emailInputLayout.run {
                        error = "Email is required"
                        isErrorEnabled = true
                    }
                }
                is LoginState.PasswordError -> {
                    loginProgressLayout.visibility = View.GONE
                    passwordInputLayout.run {
                        error = "Password is required"
                        isErrorEnabled = true
                    }
                }
            }
        }
    }

    private fun initViews() {
        emailInput.doOnTextChanged { _, _, _, _ -> emailInputLayout.isErrorEnabled = false }
        passwordInput.doOnTextChanged { _, _, _, _ -> passwordInputLayout.isErrorEnabled = false }
        button.setOnClickListener {
            viewModel.onLoginButtonClick(
                emailInput.text.toString(),
                passwordInput.text.toString()
            )
        }
        signupTV.setOnClickListener {
            Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show()
        }
    }
}