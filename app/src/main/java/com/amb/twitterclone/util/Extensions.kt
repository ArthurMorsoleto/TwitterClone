package com.amb.twitterclone.util

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.amb.twitterclone.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.regex.Pattern

object Extensions {

    private val HASHTAG_PATTERN = Pattern.compile("(#[a-zA-Z0-9ğüşöçıİĞÜŞÖÇ]{2,50}\\b)")

    fun ImageView.loadUrl(url: String?, errorDrawable: Int = R.drawable.empty) {
        context?.let {
            val options = RequestOptions()
                .placeholder(progressDrawable(context))
                .error(errorDrawable)
            Glide.with(context.applicationContext)
                .load(url)
                .apply(options)
                .into(this)
        }
    }

    fun String.getHashTags(): ArrayList<String> {
        val hashTags = arrayListOf<String>()
        val tagMatcher = HASHTAG_PATTERN.matcher(this)
        while (tagMatcher.find()) {
            tagMatcher.group(1)?.let { hashTags.add(it.removePrefix("#")) }
        }
        return hashTags
    }

    fun emptyString() = ""

    private fun progressDrawable(context: Context): CircularProgressDrawable {
        return CircularProgressDrawable(context).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }
    }
}