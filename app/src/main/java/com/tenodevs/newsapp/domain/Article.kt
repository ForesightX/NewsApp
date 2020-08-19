package com.tenodevs.newsapp.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Article(
    val author: String ?,
    val publishedAt: String ?,
    val title: String ?,
    @PrimaryKey val url: String,
    val urlToImage: String ?
) : Parcelable