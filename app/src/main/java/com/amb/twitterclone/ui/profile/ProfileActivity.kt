package com.amb.twitterclone.ui.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.amb.twitterclone.R
import com.amb.twitterclone.util.Extensions.loadUrl
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private val viewModel: ProfileViewModel by viewModels()

    private val loading: LinearLayout by lazy { findViewById(R.id.progress_profile) }
    private val userNameText: TextInputEditText by lazy { findViewById(R.id.et_username_profile) }
    private val applyButton: Button by lazy { findViewById(R.id.button_apply) }
    private val profileImage: ImageView by lazy { findViewById(R.id.image_profile) }

    var resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.dataString?.let {
                viewModel.onImageSelected(imageUri = it.toUri())
            }
        }
    }


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
        profileImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            resultLauncher.launch(intent)
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
                    profileImage.loadUrl(state.imageUrl, R.drawable.ic_person)
                }
                is ProfileViewState.UpdateError -> {
                    showMessage(getString(R.string.update_error))
                }
                is ProfileViewState.UpdateSuccess -> {
                    showMessage(getString(R.string.update_success))
                }
                is ProfileViewState.UpdateImageSuccess -> {
                    profileImage.loadUrl(state.url, R.drawable.ic_person)
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