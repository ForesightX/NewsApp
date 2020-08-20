package com.tenodevs.newsapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.tenodevs.newsapp.domain.Article
import com.tenodevs.newsapp.domain.DatabaseArticle

@Dao
interface NewsDao {

    @Query("SELECT * FROM DatabaseArticle WHERE category =:category")
    fun getArticles(category: String) : LiveData<List<DatabaseArticle>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg articles: DatabaseArticle)

    @Query("DELETE FROM DatabaseArticle WHERE category = :category")
    fun deleteAll(category: String)
}

@Database(entities = [DatabaseArticle::class], version = 1, exportSchema = false)
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
