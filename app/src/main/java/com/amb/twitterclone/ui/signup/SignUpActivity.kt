package com.amb.twitterclone.ui.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
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
class SignUpActivity : AppCompatActivity() {

    private val viewModel: SingUpViewModel by viewModels()

    private val userNameInputLayout: TextInputLayout by lazy { findViewById(R.id.til_username_sing_up) }
    private val emailInputLayout: TextInputLayout by lazy { findViewById(R.id.til_email_sing_up) }
    private val passwordInputLayout: TextInputLayout by lazy { findViewById(R.id.til_password_sing_up) }
    private val userNameInput: TextInputEditText by lazy { findViewById(R.id.et_username_sing_up) }
    private val emailInput: TextInputEditText by lazy { findViewById(R.id.et_email_sing_up) }
    private val passwordInput: TextInputEditText by lazy { findViewById(R.id.et_password_sing_up) }
    private val backButton: ImageView by lazy { findViewById(R.id.button_back) }
    private val singUpButton: Button by lazy { findViewById(R.id.button_sing_up) }
    private val loading: LinearLayout by lazy { findViewById(R.id.progress_sing_up) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initViews()
        setupObservers()
    }

    private fun initViews() {
        emailInput.doOnTextChanged { _, _, _, _ -> clearErrors() }
        passwordInput.doOnTextChanged { _, _, _, _ -> clearErrors() }
        backButton.setOnClickListener { finish() }
        singUpButton.setOnClickListener {
            viewModel.onSingUpClick(
                userNameInput.text.toString(),
                emailInput.text.toString(),
                passwordInput.text.toString()
            )
        }
    }

    private fun clearErrors() {
        userNameInputLayout.isErrorEnabled = false
        passwordInputLayout.isErrorEnabled = false
        emailInputLayout.isErrorEnabled = false
    }

    private fun setupObservers() {
        viewModel.singUpStateLiveData.observe(this) { state ->
            loading.visibility = View.GONE
            when (state) {
                SingUpViewState.EmailError -> {
                    emailInputLayout.showError(errorMessage = getString(R.string.email_required))
                }
                SingUpViewState.PasswordError -> {
                    passwordInputLayout.showError(errorMessage = getString(R.string.password_required))
                }
                SingUpViewState.UsernameError -> {
                    userNameInputLayout.showError(errorMessage = getString(R.string.username_required))
                }
                SingUpViewState.Loading -> {
                    loading.visibility = View.VISIBLE
                }
                SingUpViewState.GenericError -> {
                    Toast.makeText(this, getString(R.string.error_occurs), Toast.LENGTH_SHORT)
                        .show()
                }
                SingUpViewState.Success -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                }
                SingUpViewState.UserAlreadyRegistered -> {
                    Toast.makeText(this, getString(R.string.email_registered), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun TextInputLayout.showError(errorMessage: String) {
        this.run {
            error = errorMessage
            isErrorEnabled = true
        }
    }

    companion object {
        fun newInstance(context: Context) = Intent(context, SignUpActivity::class.java)
    }
}