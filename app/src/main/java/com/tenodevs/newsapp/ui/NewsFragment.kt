package com.tenodevs.newsapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.tenodevs.newsapp.R
import com.tenodevs.newsapp.databinding.FragmentNewsBinding

class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding
    private var position = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false)

        arguments?.takeIf { it.containsKey("POSITION") }?.apply {
            position = getInt("POSITION")
            binding.textView.text = position.toString()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Log.i("Headlines", "onStart called by $position")
    }

    override fun onResume() {
        super.onResume()
        Log.i("Headlines", "onResume called by $position")
    }

    override fun onPause() {
        super.onPause()
        Log.i("Headlines", "onPause called by $position")
    }

}