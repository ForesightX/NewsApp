package com.tenodevs.newsapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tenodevs.newsapp.network.NewsFilter
import com.tenodevs.newsapp.repository.NewsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

enum class Status { LOADING, DONE, ERROR }

class NewsViewModel(application: Application, private val category: NewsFilter) :
    AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()
    private val mRepository: NewsRepository = NewsRepository(application)
    val status = mRepository.status
    val newsList = mRepository.newsList

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        getFilteredHeadlines()
    }

    fun getFilteredHeadlines() {
        viewModelScope.launch {
            mRepository.getFilteredHeadlines(category)
        }
    }

    fun refreshDataAction() {
        mRepository.refreshDataAction()
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