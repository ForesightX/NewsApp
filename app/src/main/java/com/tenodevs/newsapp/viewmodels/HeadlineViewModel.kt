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
import retrofit2.HttpException

class HeadlineViewModel(application: Application) : AndroidViewModel(application) {
    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val _newsList = MutableLiveData<List<Article>>()
    val newsList: LiveData<List<Article>>
        get() = _newsList

    init {
        getFilteredHeadlines(NewsFilter.HEADLINE)
    }

    private fun getFilteredHeadlines(category: NewsFilter) {
        viewModelScope.launch {
            val getNewsItems = NewsAPI.retrofitService.getLatestHeadlineAsync(category.value)

            try {
                val listResult = getNewsItems.await()
                _newsList.value = listResult.articles
                Log.i("FETCH_NEWS", "Success : ${listResult.totalResults} news fetched.")
            } catch (e : HttpException) {
                Log.e("FETCH_NEWS", "Failure: ${e.message()}")
            }
        }
    }
    fun updateNewsWithCategory(filter: NewsFilter) {
        getFilteredHeadlines(filter)
    }


    class HeadlineViewModelFactory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HeadlineViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HeadlineViewModel(application) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}