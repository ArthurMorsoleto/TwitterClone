package com.amb.twitterclone.ui.home.sections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.amb.twitterclone.R
import com.amb.twitterclone.ui.home.sections.adapters.TweetListAdapter
import com.amb.twitterclone.util.Extensions.emptyString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : SectionFragment() {

    private val searchFragmentViewModel: SearchFragmentViewModel by viewModels()

    private val tweetList: RecyclerView? by lazy { view?.findViewById(R.id.rv_tweet_list) }
    private val swipeRefreshLayout: SwipeRefreshLayout? by lazy { view?.findViewById(R.id.swipe_refresh_tweet_list) }
    private val tweetListAdapter by lazy { TweetListAdapter(arrayListOf()) }
    private var currentHashTag = emptyString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupObserver()
    }

    private fun initView() {
        tweetList?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tweetListAdapter
        }
        swipeRefreshLayout?.apply {
            setOnRefreshListener {
                isRefreshing = false
                searchFragmentViewModel.onNewTermSearched(currentHashTag)
            }
        }
    }

    private fun setupObserver() {
        searchFragmentViewModel.searchTweetLiveData.observe(viewLifecycleOwner) {
            if (it is TweetListViewState.ShowList) {
                tweetListAdapter.updateList(it.tweets)
            } else {
                Toast.makeText(context, "Error Fetching Tweets", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun newHashtag(term: String) {
        currentHashTag = term
        searchFragmentViewModel.onNewTermSearched(currentHashTag)
    }
}