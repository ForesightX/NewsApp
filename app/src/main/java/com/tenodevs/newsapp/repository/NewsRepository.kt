package com.tenodevs.newsapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tenodevs.newsapp.database.NewsDatabase
import com.tenodevs.newsapp.domain.Article
import com.tenodevs.newsapp.domain.toDatabaseModel
import com.tenodevs.newsapp.domain.toDomainModel
import com.tenodevs.newsapp.network.NewsAPI
import com.tenodevs.newsapp.viewmodels.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception


class NewsRepository(
    private val app: Application, private val database: NewsDatabase, private val category: String
) {

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    val newsList: LiveData<List<Article>> = Transformations.map(database.newsDao.getArticles(category)) {
        it.toDomainModel()
    }

    suspend fun getFilteredHeadlines() {
        withContext(Dispatchers.IO) {
            val getNewsItems = NewsAPI.retrofitService.getLatestHeadlineAsync(category)

            try {
                _status.postValue(Status.LOADING)

                val listResult = getNewsItems.await()

                val listDatabaseArticle = listResult.articles.toDatabaseModel(category)

                _status.postValue(Status.DONE)
                database.newsDao.insertAll(*listDatabaseArticle)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _status.value = Status.ERROR
                }
            }
        }
    }

}
