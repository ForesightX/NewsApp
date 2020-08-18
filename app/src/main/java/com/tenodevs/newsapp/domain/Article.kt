package com.tenodevs.newsapp.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Article(
    val author: String ?,
    val content: String ?,
    val description: String ?,
    val publishedAt: String ?,
    val source: Source ?,
    val title: String ?,
    val url: String,
    val urlToImage: String ?
) : Parcelable