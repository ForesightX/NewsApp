package com.tenodevs.newsapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.tenodevs.newsapp.domain.Article

@Dao
interface NewsDao {

    @Query("SELECT * FROM Article")
    fun getArticles() : LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg articles: Article)

    @Query("DELETE FROM Article")
    fun deleteAll()
}

@Database(entities = [Article::class], version = 1, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {
    abstract val newsDao: NewsDao
}

private lateinit var INSTANCE: NewsDatabase

fun getDatabase(context: Context) : NewsDatabase {
    synchronized(NewsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                NewsDatabase::class.java,
                "news_database"
            ).build()
        }
    }
    return INSTANCE
}
