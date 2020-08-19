package com.tenodevs.newsapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tenodevs.newsapp.domain.Article
import com.tenodevs.newsapp.domain.toDatabaseModel
import com.tenodevs.newsapp.network.NewsAPI
import com.tenodevs.newsapp.network.NewsFilter
import com.tenodevs.newsapp.viewmodels.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*


class NewsRepository(private val app: Application) {

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private val _newsList = MutableLiveData<List<Article>>()
    val newsList: LiveData<List<Article>>
        get() = _newsList

    suspend fun getFilteredHeadlines(category: NewsFilter) {
        withContext(Dispatchers.IO) {
            val getNewsItems = NewsAPI.retrofitService.getLatestHeadlineAsync(category.value)

            try {
                withContext(Dispatchers.Main) {
                    _status.value = Status.LOADING
                }

                val listResult = getNewsItems.await()

                val listDatabaseArticle = listResult.articles.toDatabaseModel(category.value)

                withContext(Dispatchers.Main) {
                    _status.value = Status.DONE
                    _newsList.value = listResult.articles
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _status.value = Status.ERROR
                }
            }
        }
    }

    fun refreshDataAction() {
        _newsList.value = ArrayList()
    }
}
