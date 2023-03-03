package com.amb.twitterclone.ui.tweet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.amb.twitterclone.R
import com.amb.twitterclone.databinding.ActivityTweetBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Add new Tweet Screen.
 */
@AndroidEntryPoint
class TweetActivity : AppCompatActivity() {

    private val viewModel: TweetViewModel by viewModels()

    private lateinit var viewBinding: ActivityTweetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityTweetBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        initViews()
        setupObservers()
    }

    private fun initViews() {
        with(viewBinding) {
            btSendTweet.apply {
                setOnClickListener {
                    viewModel.onSendTweetClick(
                        textContent = etAddTweet.text.toString()
                    )
                }
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
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT)
            .show()
    }

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, TweetActivity::class.java)
        }
    }
}