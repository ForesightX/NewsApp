package com.tenodevs.newsapp.domain

import android.os.Parcelable
import androidx.lifecycle.Transformations.map
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Article(
    val author: String ?,
    val publishedAt: String ?,
    val title: String ?,
    @PrimaryKey val url: String,
    val urlToImage: String ?
) : Parcelable

@Entity
data class DatabaseArticle (
    val category: String,
    val author: String ?,
    val publishedAt: String ?,
    val title: String ?,
    @PrimaryKey val url: String,
    val urlToImage: String ?
)

fun List<Article>.toDatabaseModel(category: String) : List<DatabaseArticle> {
    return this.map {
        DatabaseArticle(
            category = category,
            author = it.author,
            publishedAt = it.publishedAt,
            title = it.title,
            url = it.url,
            urlToImage = it.urlToImage
        )
    }
}

fun List<DatabaseArticle>.toDomainModel() : List<Article> {
    return this.map {
        Article(
            author = it.author,
            publishedAt = it.publishedAt,
            title = it.title,
            url = it.url,
            urlToImage = it.urlToImage
        )
    }
}