package com.tenodevs.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tenodevs.newsapp.databinding.ItemNewsBinding
import com.tenodevs.newsapp.domain.Article

class NewsListener(val clickListener: (url: String) -> Unit) {
    fun onClick(article: Article) = clickListener(article.url)
}


class NewsAdapter(private val clickListener: NewsListener)
    : ListAdapter<Article, NewsAdapter.NewsViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article, clickListener)
    }

    companion object DiffCallback: DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    class NewsViewHolder private constructor(private var binding: ItemNewsBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article, clickListener: NewsListener) {
            binding.article = article
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): NewsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemNewsBinding.inflate(layoutInflater, parent, false)
                return NewsViewHolder(binding)
            }
        }
    }

}