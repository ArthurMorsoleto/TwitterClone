package com.amb.twitterclone.ui.home

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.amb.twitterclone.R
import com.amb.twitterclone.ui.home.sections.HomeFragment
import com.amb.twitterclone.ui.home.sections.MyActivityFragment
import com.amb.twitterclone.ui.home.sections.SearchFragment
import com.amb.twitterclone.ui.profile.ProfileActivity
import com.amb.twitterclone.ui.tweet.TweetActivity
import com.amb.twitterclone.util.Extensions.loadUrl
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Screen for TwitterClone
 */
@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()

    private val logoutButton: ImageView by lazy { findViewById(R.id.button_logout) }
    private val logo: ImageView by lazy { findViewById(R.id.image_logo) }
    private val searchBar: CardView by lazy { findViewById(R.id.card_search_bar) }
    private val searchText: EditText by lazy { findViewById(R.id.edit_search) }
    private val sectionsViewPager: ViewPager2 by lazy { findViewById(R.id.vp_sections) }
    private val tabLayout: TabLayout by lazy { findViewById(R.id.tab_layout) }
    private val userImage: ImageView by lazy { findViewById(R.id.image_user) }
    private val newTweet: FloatingActionButton by lazy { findViewById(R.id.bt_add_new_tweet) }

    private val searchFragment by lazy { SearchFragment() }
    private val myActivityFragment by lazy { MyActivityFragment() }
    private val homeFragment by lazy { HomeFragment() }
    private var homeSectionsAdapter: HomeSectionsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initViews()
        setupObservers()
        viewModel.onViewReady()
    }

    private fun setupObservers() {
        viewModel.profileImageLiveData.observe(this) {
            userImage.loadUrl(it, R.drawable.ic_person)
        }
    }

    private fun initViews() {
        logoutButton.setOnClickListener {
            viewModel.onLogoutButtonClick()
            finish()
        }
        userImage.setOnClickListener {
            startActivity(ProfileActivity.newInstance(this))
        }
        homeSectionsAdapter = HomeSectionsAdapter(
            fragmentActivity = this,
            items = listOf(homeFragment, searchFragment, myActivityFragment)
        )
        sectionsViewPager.adapter = homeSectionsAdapter
        sectionsViewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
            override fun onTabSelected(tab: TabLayout.Tab) {
                sectionsViewPager.currentItem = tab.position
                when (tab.position) {
                    0 -> {
                        logo.visibility = View.VISIBLE
                        searchBar.visibility = View.GONE
                    }
                    1 -> {
                        logo.visibility = View.GONE
                        searchBar.visibility = View.VISIBLE
                    }
                    else -> {
                        logo.visibility = View.VISIBLE
                    }
                }
            }
        })
        searchText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchFragment.newHashtag(term = v?.text.toString())
            }
            true
        }
        newTweet.setOnClickListener {
            TweetActivity.newInstance(this@HomeActivity).run(::startActivity)
        }
    }
}