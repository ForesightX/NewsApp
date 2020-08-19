package com.tenodevs.newsapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.tenodevs.newsapp.domain.Article
import com.tenodevs.newsapp.network.NewsAPI
import com.tenodevs.newsapp.network.NewsFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

enum class Status { LOADING, DONE, ERROR }

class NewsViewModel(application: Application, private val category: NewsFilter) :
    AndroidViewModel(application) {
    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private val _newsList = MutableLiveData<List<Article>>()
    val newsList: LiveData<List<Article>>
        get() = _newsList

    init {
        getFilteredHeadlines()
    }

    fun getFilteredHeadlines() {
        viewModelScope.launch {
            val getNewsItems = NewsAPI.retrofitService.getLatestHeadlineAsync(category.value)

            try {
                _status.value = Status.LOADING
                val listResult = getNewsItems.await()
                _status.value = Status.DONE
                _newsList.value = listResult.articles
                Log.i("FETCH_NEWS", "Success : ${listResult.totalResults} news fetched.")
            } catch (e: Exception) {
                _status.value = Status.ERROR
                Log.e("FETCH_NEWS", "Failure: ${e.message}")
            }
        }
    }

    fun refreshDataAction() {
        _newsList.value = ArrayList()
    }

    class HeadlineViewModelFactory(private val application: Application, val position: Int) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NewsViewModel(
                    application, when (position) {
                        0 -> NewsFilter.HEADLINE
                        1 -> NewsFilter.BUSINESS
                        2 -> NewsFilter.ENTERTAINMENT
                        3 -> NewsFilter.HEALTH
                        else -> NewsFilter.SPORT
                    }
                ) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}