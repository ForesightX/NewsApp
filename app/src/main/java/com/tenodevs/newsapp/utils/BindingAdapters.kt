package com.tenodevs.newsapp.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tenodevs.newsapp.R
import com.tenodevs.newsapp.adapters.NewsAdapter
import com.tenodevs.newsapp.domain.Article

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()

        Picasso.get().load(imgUri)
            .error(R.drawable.ic_broken_image)
            .placeholder(R.drawable.loading_animation)
            .into(imgView)
    }
}

@BindingAdapter("list")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Article>?) {
    val adapter = recyclerView.adapter as NewsAdapter

    adapter.submitList(data)
}
