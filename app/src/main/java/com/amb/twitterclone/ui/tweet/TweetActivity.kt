package com.amb.twitterclone.ui.tweet

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.amb.camera.CameraActivityContract
import com.amb.twitterclone.R
import com.amb.twitterclone.databinding.ActivityTweetBinding
import com.amb.twitterclone.ui.dialog.PhotoPickDialog
import com.amb.twitterclone.ui.dialog.PhotoPickListener
import com.amb.twitterclone.util.Extensions.loadUrl
import com.amb.twitterclone.util.INTENT_TYPE_IMAGE
import dagger.hilt.android.AndroidEntryPoint

/**
 * Add new Tweet Screen.
 */
@AndroidEntryPoint
class TweetActivity : AppCompatActivity(), PhotoPickListener {

    private val viewModel: TweetViewModel by viewModels()

    private lateinit var viewBinding: ActivityTweetBinding

    private val cameraLauncher = registerForActivityResult(
        CameraActivityContract()
    ) { result ->
        if (result != CameraActivityContract.CAMERA_ERROR_RESULT) {
            viewModel.onImageAdded(result.toUri())
        }
    }
    private var pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.dataString?.let {
                viewModel.onImageAdded(it.toUri())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityTweetBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        initViews()
        setupObservers()
    }

    private fun initViews() {
        with(viewBinding) {
            btSendTweet.setOnClickListener {
                viewModel.onSendTweetClick(textContent = etAddTweet.text.toString())
            }
            btAddImage.setOnClickListener {
                PhotoPickDialog(this@TweetActivity).show()
            }
        }
    }

    private fun setupObservers() {
        viewModel.tweetViewState.observe(this) { viewState ->
            when (viewState) {
                is TweetViewState.Sent -> {
                    showMessage(getString(R.string.tweet_sent))
                    finish()
                }
                is TweetViewState.Error -> {
                    showMessage(getString(R.string.error_occurs))
                }
                is TweetViewState.EmptyContent -> {
                    showMessage(getString(R.string.tweet_no_content))
                }
                is TweetViewState.PreviewImage -> {
                    viewBinding.ivNewTweet.loadUrl(viewState.uri.toString())
                }
            }
        }
        viewModel.tweetViewActions.observe(this) { viewAction ->
            when (viewAction) {
                is TweetViewActions.OpenCamera -> {
                    cameraLauncher.launch()
                }
                is TweetViewActions.OpenGallery -> {
                    val intent = Intent(Intent.ACTION_PICK).apply { type = INTENT_TYPE_IMAGE }
                    pickImageLauncher.launch(intent)
                }
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onTakePhotoClick() {
        viewModel.onTakePhotoClick()
    }

    override fun onChoosePhotoClick() {
        viewModel.onChoosePhotoClick()
    }

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, TweetActivity::class.java)
        }
    }
}