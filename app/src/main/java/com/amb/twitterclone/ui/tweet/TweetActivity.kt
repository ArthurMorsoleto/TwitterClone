package com.amb.twitterclone.ui.tweet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amb.twitterclone.R

class TweetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet)
    }

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, TweetActivity::class.java)
        }
    }
}