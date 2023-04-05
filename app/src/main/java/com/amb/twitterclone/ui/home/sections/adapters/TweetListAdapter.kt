package com.amb.twitterclone.ui.home.sections.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amb.twitterclone.R
import com.amb.twitterclone.domain.model.Tweet
import com.amb.twitterclone.util.Extensions.loadUrl
import java.text.SimpleDateFormat
import java.util.*

/**
 * Adapter class for Tweet List
 */
class TweetListAdapter(
    private val tweetList: ArrayList<Tweet>
) : RecyclerView.Adapter<TweetListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_tweet, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(tweetList[position])
    }

    override fun getItemCount() = tweetList.size

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private val userName: TextView by lazy { v.findViewById(R.id.text_tweet_username) }
        private val text: TextView by lazy { v.findViewById(R.id.text_tweet_content) }
        private val image: ImageView by lazy { v.findViewById(R.id.image_tweet_content) }
        private val date: TextView by lazy { v.findViewById(R.id.text_tweet_date) }
        private val likes: ImageView by lazy { v.findViewById(R.id.image_likes) }
        private val likesCount: TextView by lazy { v.findViewById(R.id.text_tweet_likes_count) }
        private val retweets: ImageView by lazy { v.findViewById(R.id.image_retweets) }
        private val retweetsCount: TextView by lazy { v.findViewById(R.id.text_tweet_retweets_count) }

        fun bindView(tweet: Tweet) {
            userName.text = tweet.userName
            text.text = tweet.text
            date.text = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.UK).format(
                Date(tweet.timestamp)
            )
            if (tweet.imageUrl.isNotBlank()) {
                image.loadUrl(tweet.imageUrl)
            }
            likesCount.text = tweet.likes.size.toString()
            retweetsCount.text = (tweet.userIds.size - 1).toString()
        }
    }
}