package com.tenodevs.newsapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.tenodevs.newsapp.R
import com.tenodevs.newsapp.adapters.NewsAdapter
import com.tenodevs.newsapp.databinding.FragmentHeadlinesBinding
import com.tenodevs.newsapp.viewmodels.HeadlineViewModel

class HeadlinesFragment : Fragment() {

    private lateinit var binding: FragmentHeadlinesBinding

    private val viewModel: HeadlineViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, HeadlineViewModel.HeadlineViewModelFactory(activity.application))
            .get(HeadlineViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_headlines, container, false
        )

        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.viewModel = viewModel
        binding.recyclerView.adapter = NewsAdapter()

        // This adds a line divider
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(decoration)
    }

}