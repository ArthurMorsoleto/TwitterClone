package com.amb.twitterclone.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.amb.twitterclone.ui.home.sections.SectionFragment

class HomeSectionsAdapter(
    private val fragmentActivity: FragmentActivity,
    private val items: List<SectionFragment>
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = items.size

    override fun createFragment(position: Int): Fragment {
        return items[position]
    }
}