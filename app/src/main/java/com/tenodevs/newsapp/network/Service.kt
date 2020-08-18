package com.tenodevs.newsapp.network


import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.tenodevs.newsapp.domain.News
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "c5f0ab174f8d49b4989e405d8e97a9c5"
private const val BASE_URL = "https://newsapi.org"

enum class NewsFilter(val value: String) {
    HEADLINE("General"),
    BUSINESS("business"),
    ENTERTAINMENT("entertainment"),
    HEALTH("health"),
    SPORT("sport")
}

val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface NewsAPIService {

    @GET("/v2/top-headlines")
    fun getLatestHeadlineAsync(
        @Query("category") category : String, //General, business, entertainment, health, science, sports, technology
        @Query("country") country : String = "ng",
        @Query("pageSize") pageSize : Int = 100,
        @Query("apikey") apikey : String = API_KEY
    ) : Deferred<News>
}

object NewsAPI {
    val retrofitService : NewsAPIService by lazy {
        retrofit.create(NewsAPIService::class.java)
    }
}
