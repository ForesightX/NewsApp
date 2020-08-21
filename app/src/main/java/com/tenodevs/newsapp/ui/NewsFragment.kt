package com.tenodevs.newsapp.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.tenodevs.newsapp.R
import com.tenodevs.newsapp.adapters.NewsAdapter
import com.tenodevs.newsapp.adapters.TAB_POSITION
import com.tenodevs.newsapp.databinding.FragmentNewsBinding
import com.tenodevs.newsapp.viewmodels.NewsViewModel
import com.tenodevs.newsapp.viewmodels.Status

class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding
    private var position = 0

    private val viewModel: NewsViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(
            this, NewsViewModel.HeadlineViewModelFactory(
                activity.application, position
            )
        ).get(NewsViewModel::class.java)
    }

    private val snackBar: Snackbar by lazy {
        Snackbar.make(
            binding.recyclerView,
            getString(R.string.snackbar_error),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(R.string.sncakbar_action) {
                viewModel.getFilteredHeadlines()
            }
            .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.colorActionText))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_news, container, false
        )
        binding.lifecycleOwner = this

        arguments?.takeIf {
            it.containsKey(TAB_POSITION)
        }?.apply {
            position = getInt(TAB_POSITION)
        }

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> viewModel.getFilteredHeadlines()
        }
        return true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.apply {
            viewModel = this@NewsFragment.viewModel

            // This adds a line divider
            val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            recyclerView.adapter = NewsAdapter()
            recyclerView.addItemDecoration(decoration)

            swipeRefresh.setColorSchemeColors(Color.WHITE)

            swipeRefresh.setOnRefreshListener {
                this@NewsFragment.viewModel.apply {
                    getFilteredHeadlines()
                }
                swipeRefresh.isRefreshing = false
            }
        }

        viewModel.status.observe(viewLifecycleOwner, Observer {
            when (it) {
                Status.LOADING -> {
                    snackBar.dismiss()
                    binding.apply {
                        recyclerView.visibility = View.GONE
                        statusImageView.visibility = View.VISIBLE
                        statusImageView.setImageResource(R.drawable.loading_animation)
                    }
                }
                Status.DONE -> {
                    snackBar.dismiss()
                    binding.apply {
                        recyclerView.visibility = View.VISIBLE
                        statusImageView.visibility = View.GONE
                    }
                }
                else -> {
                    snackBar.show()
                    if (binding.recyclerView.adapter?.itemCount == 0) {
                        binding.apply {
                            statusImageView.visibility = View.VISIBLE
                            statusImageView.setImageResource(R.drawable.ic_connection_error)
                        }
                    } else {
                        binding.apply {
                            recyclerView.visibility = View.VISIBLE
                            statusImageView.visibility = View.GONE
                        }
                    }
                }
            }

            /*when(status) {
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    statusImageView.visibility = View.VISIBLE
                    statusImageView.setImageResource(R.drawable.ic_connection_error)
                }
                Status.DONE -> {

                }
            }*/
        })

        this.context?.let {
            ContextCompat.getColor(
                it, R.color.colorPrimaryDark
            )
        }?.let { binding.swipeRefresh.setProgressBackgroundColorSchemeColor(it) }

    }

}