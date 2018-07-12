package at.ict4d.ict4dnews.persistence.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import at.ict4d.ict4dnews.models.NEWS_TABLE_TABLE_NAME
import at.ict4d.ict4dnews.models.NewsModel
import io.reactivex.Flowable

@Dao
abstract class NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(news: NewsModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(news: List<NewsModel>)

    @Query("SELECT * FROM $NEWS_TABLE_TABLE_NAME")
    abstract fun getAll(): LiveData<List<NewsModel>>
}