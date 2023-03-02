package com.amb.twitterclone.ui.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.amb.camera.CameraActivityContract
import com.amb.twitterclone.R
import com.amb.twitterclone.databinding.ActivityProfileBinding
import com.amb.twitterclone.ui.dialog.PhotoPickDialog
import com.amb.twitterclone.ui.dialog.PhotoPickListener
import com.amb.twitterclone.ui.login.LoginActivity
import com.amb.twitterclone.util.Extensions.loadUrl
import com.amb.twitterclone.util.INTENT_TYPE_IMAGE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity(), PhotoPickListener {

    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var viewBinding: ActivityProfileBinding

    private var resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.dataString?.let {
                viewModel.onImageSelected(it.toUri())
            }
        }
    }
    private val cameraLauncher = registerForActivityResult(CameraActivityContract()) { result ->
        if (result != CameraActivityContract.CAMERA_ERROR_RESULT) {
            viewModel.onImageSelected(result.toUri())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        initViews()
        setupObservers()
        viewModel.onViewReady()
    }

    override fun onTakePhotoClick() {
        cameraLauncher.launch()
    }

    override fun onChoosePhotoClick() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = INTENT_TYPE_IMAGE
        resultLauncher.launch(intent)
    }

    private fun initViews() {
        with(viewBinding) {
            buttonApply.setOnClickListener {
                viewModel.onApply(etUsernameProfile.text.toString())
            }
            imageProfile.setOnClickListener {
                PhotoPickDialog(this@ProfileActivity).show()
            }
            textSignout.setOnClickListener {
                viewModel.onSingoutClick()
                startActivity(
                    LoginActivity.newInstance(this@ProfileActivity)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
            }
        }
    }

    private fun setupObservers() {
        viewModel.profileViewState.observe(this) { state ->
            viewBinding.progressProfile.visibility = View.GONE
            when (state) {
                is ProfileViewState.Loading -> viewBinding.progressProfile.visibility = View.VISIBLE
                is ProfileViewState.EmptyData -> showErrorAndFinish()
                is ProfileViewState.GenericError -> showErrorAndFinish()
                is ProfileViewState.ProfileData -> {
                    viewBinding.etUsernameProfile.setText(
                        state.userName, TextView.BufferType.EDITABLE
                    )
                    viewBinding.imageProfile.loadUrl(state.imageUrl, R.drawable.ic_person)
                }
                is ProfileViewState.UpdateError -> {
                    showMessage(getString(R.string.update_error))
                }
                is ProfileViewState.UpdateSuccess -> {
                    showMessage(getString(R.string.update_success))
                }
                is ProfileViewState.UpdateImageSuccess -> {
                    viewBinding.imageProfile.loadUrl(state.url, R.drawable.ic_person)
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