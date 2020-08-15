package com.tenodevs.newsapp.adapters

import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tenodevs.newsapp.ui.*

class NewsPagerAdapter(fa: FragmentActivity, private val count: Int) : FragmentStateAdapter(fa) {

    override fun getItemCount() = count

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HeadlinesFragment()
            1 -> BusinessFragment()
            2 -> EntertainmentFragment()
            3 -> HealthFragment()
            else -> SportFragment()
        }
    }
}

