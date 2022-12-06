package com.amb.twitterclone.ui.home

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.amb.twitterclone.R
import com.amb.twitterclone.ui.home.sections.HomeFragment
import com.amb.twitterclone.ui.home.sections.MyActivityFragment
import com.amb.twitterclone.ui.home.sections.SearchFragment
import com.amb.twitterclone.ui.profile.ProfileActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()

    private val logoutButton: ImageView by lazy { findViewById(R.id.button_logout) }
    private val sectionsViewPager: ViewPager2 by lazy { findViewById(R.id.vp_sections) }
    private val tabLayout: TabLayout by lazy { findViewById(R.id.tab_layout) }
    private val userImage: ImageView by lazy { findViewById(R.id.image_user) }

    private var homeSectionsAdapter: HomeSectionsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initViews()
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
            items = listOf(HomeFragment(), SearchFragment(), MyActivityFragment())
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
            }
        })
    }
}