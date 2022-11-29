package com.amb.twitterclone.ui.home

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.amb.twitterclone.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()

    private val logoutButton: ImageView by lazy { findViewById(R.id.button_logout) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        logoutButton.setOnClickListener {
            viewModel.onLogoutButtonClick()
            finish()
        }
    }
}