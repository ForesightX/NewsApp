package com.tenodevs.newsapp.adapters

import android.os.Bundle
import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tenodevs.newsapp.ui.*

class NewsPagerAdapter(fa: FragmentActivity, private val count: Int) : FragmentStateAdapter(fa) {

    override fun getItemCount() = count

    override fun createFragment(position: Int): Fragment {
        val fragment = NewsFragment()
        fragment.arguments = Bundle().apply {
            putInt("POSITION", position)
        }
        return fragment
    }
}

