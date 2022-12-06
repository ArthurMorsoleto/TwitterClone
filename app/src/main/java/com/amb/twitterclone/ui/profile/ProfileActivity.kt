package com.amb.twitterclone.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.amb.twitterclone.R
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private val viewModel: ProfileViewModel by viewModels()

    private val loading: LinearLayout by lazy { findViewById(R.id.progress_profile) }
    private val userNameText: TextInputEditText by lazy { findViewById(R.id.et_username_profile) }
    private val applyButton: Button by lazy { findViewById(R.id.button_apply) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViews()
        setupObservers()
        viewModel.onViewReady()
    }

    private fun initViews() {
        applyButton.setOnClickListener {
            viewModel.onApply(newUserName = userNameText.text.toString())
        }
    }

    private fun setupObservers() {
        viewModel.profileViewState.observe(this) { state ->
            loading.visibility = View.GONE
            when (state) {
                is ProfileViewState.Loading -> loading.visibility = View.VISIBLE
                is ProfileViewState.EmptyData -> showErrorAndFinish()
                is ProfileViewState.GenericError -> showErrorAndFinish()
                is ProfileViewState.ProfileData -> {
                    userNameText.setText(state.userName, TextView.BufferType.EDITABLE)
                    /*
                     TODO
                        imageUrl = user?.imageUrl
                        imageUrl?.let {
                        photoIV.loadUrl(user?.imageUrl, R.drawable.logo)
                    */
                }
                is ProfileViewState.UpdateError -> {
                    showMessage(getString(R.string.error_profile_data))
                    showMessage(getString(R.string.update_error))
                }
                is ProfileViewState.UpdateSuccess -> {
                    showMessage(getString(R.string.update_success))
                }
            }
        }
    }

    private fun showErrorAndFinish() {
        showMessage(getString(R.string.error_profile_data))
        finish()
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance(context: Context) = Intent(context, ProfileActivity::class.java)
    }
}