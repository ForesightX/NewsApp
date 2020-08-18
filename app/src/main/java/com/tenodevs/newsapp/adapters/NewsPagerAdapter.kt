package com.tenodevs.newsapp.adapters

import android.os.Bundle
import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tenodevs.newsapp.ui.*

const val TAB_POSITION: String = "position"

class NewsPagerAdapter(fa: FragmentActivity, private val count: Int) : FragmentStateAdapter(fa) {

    override fun getItemCount() = count

    override fun createFragment(position: Int): Fragment {
        val fragment = HeadlinesFragment()
        fragment.arguments = Bundle().apply {
            putInt(TAB_POSITION, position)
        }
        return fragment
    }
}

