package com.amb.twitterclone.ui.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.BaseBundle
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.amb.camera.CameraActivity
import com.amb.twitterclone.R
import com.amb.twitterclone.ui.dialog.PhotoPickDialog
import com.amb.twitterclone.ui.dialog.PhotoPickListener
import com.amb.twitterclone.ui.login.LoginActivity
import com.amb.twitterclone.util.Extensions.loadUrl
import com.amb.twitterclone.util.INTENT_TYPE_IMAGE
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity(), PhotoPickListener {

    private val viewModel: ProfileViewModel by viewModels()

    private val loading: LinearLayout by lazy { findViewById(R.id.progress_profile) }
    private val userNameText: TextInputEditText by lazy { findViewById(R.id.et_username_profile) }
    private val applyButton: Button by lazy { findViewById(R.id.button_apply) }
    private val signOutButton: TextView by lazy { findViewById(R.id.text_signout) }
    private val profileImage: ImageView by lazy { findViewById(R.id.image_profile) }

    private var resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.dataString?.let {
                viewModel.onImageSelected(it.toUri())
            }
        }
    }
    private var cameraLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.extras.let {
                val uri = (it as BaseBundle).get(CameraActivity.CAMERA_RESULT_SUCCESS)
                    .toString().toUri()
                viewModel.onImageSelected(uri)
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

    override fun onTakePhotoClick() {
        cameraLauncher.launch(CameraActivity.newInstance(this))
    }

    override fun onChoosePhotoClick() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = INTENT_TYPE_IMAGE
        resultLauncher.launch(intent)
    }

    private fun initViews() {
        applyButton.setOnClickListener {
            viewModel.onApply(userNameText.text.toString())
        }
        profileImage.setOnClickListener {
            PhotoPickDialog(this).show()
        }
        signOutButton.setOnClickListener {
            viewModel.onSingoutClick()
            startActivity(
                LoginActivity.newInstance(this)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
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