package com.amb.twitterclone.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.amb.twitterclone.R
import com.amb.twitterclone.ui.home.HomeActivity
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
            //TODO continue flow
        }
    }

    private fun setupObserver() {
        viewModel.loginStateLiveData.observe(this) { state ->
            loginProgressLayout.visibility = View.GONE
            when (state) {
                is LoginViewState.Success -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                }
                is LoginViewState.Failure -> {
                    Toast.makeText(this, "an error occurs", Toast.LENGTH_SHORT).show()
                }
                is LoginViewState.Loading -> {
                    loginProgressLayout.visibility = View.VISIBLE
                }
                is LoginViewState.EmailError -> {
                    showEmailError("email is required")
                }
                is LoginViewState.PasswordError -> {
                    showPasswordError("password is required")
                }
                is LoginViewState.InvalidCredentials -> {
                    showEmailError("email or password is invalid")
                    showPasswordError("email or password is invalid")
                }
                is LoginViewState.UnknownUser -> {
                    showEmailError("email not found")
                }
            }
        }
    }

    private fun showPasswordError(s: String) {
        passwordInputLayout.run {
            error = s
            isErrorEnabled = true
        }
    }

    private fun showEmailError(s: String) {
        emailInputLayout.run {
            error = s
            isErrorEnabled = true
        }
    }
}