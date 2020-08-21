package com.tenodevs.newsapp.repository

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
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
import java.util.*
import java.util.concurrent.TimeUnit


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
        // TODO: Use sharedPreferences
        //  Check the time the news were last updated
        //  If 0L is returned, this means the user is updating the news for the very first time
        //  Else If the difference between the currentTime and the lastUpdateTime is less then 30 minutes, no need to make the network call
        //  Else the cached news are outdated, and a new network call is required
        val sharedPref = app.getSharedPreferences("LastUpdateTime", Context.MODE_PRIVATE)
        val lastUpdateTime = sharedPref.getLong(category, 0L)

        if (lastUpdateTime == 0L) {
            loadFilteredHeadlines(sharedPref)
        } else {
            val currentTime = Date().time
            if (currentTime.minus(lastUpdateTime) > TimeUnit.MINUTES.toMillis(30L)) {
                loadFilteredHeadlines(sharedPref)

                Log.i("NewsRepository by $category", "Made a network call")
            } else {
                // MAKE NO NETWORK CALL?
                Log.i("NewsRepository by $category", "No need to make the network call")
            }
        }

    }

    private suspend fun loadFilteredHeadlines(sharedPref: SharedPreferences) {
        withContext(Dispatchers.IO) {
            val getNewsItems = NewsAPI.retrofitService.getLatestHeadlineAsync(category)

            try {
                _status.postValue(Status.LOADING)

                val listResult = getNewsItems.await()

                val listDatabaseArticle = listResult.articles.toDatabaseModel(category)

                _status.postValue(Status.DONE)
                database.newsDao.deleteAll(category)
                sharedPref.edit().putLong(category, Date().time).apply()
                database.newsDao.insertAll(*listDatabaseArticle)
            } catch (e: Exception) {
                _status.postValue(Status.ERROR)
            }
        }
    }
}
