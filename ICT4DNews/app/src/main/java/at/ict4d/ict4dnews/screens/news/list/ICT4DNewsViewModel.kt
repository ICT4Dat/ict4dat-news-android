package at.ict4d.ict4dnews.screens.news.list

import android.arch.lifecycle.MutableLiveData
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.models.NewsListModel
import at.ict4d.ict4dnews.models.wordpress.SelfHostedWPPost
import at.ict4d.ict4dnews.models.wordpress.WordpressAuthor
import at.ict4d.ict4dnews.models.wordpress.WordpressMedia
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.screens.base.BaseViewModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ICT4DNewsViewModel : BaseViewModel() {

    val newsListData: MutableLiveData<List<NewsListModel>> = MutableLiveData()

    @Inject
    protected lateinit var persistenceManager: IPersistenceManager

    init {
        ICT4DNewsApplication.component.inject(this)

        compositeDisposable.add(Flowable.zip(
                persistenceManager.getAllSelfHostedWPPosts(),
                persistenceManager.getAllWordpressAuthors(),
                persistenceManager.getAllWordpressMedia(),
                Function3() { posts: List<SelfHostedWPPost>, authors: List<WordpressAuthor>, media: List<WordpressMedia> ->

                    val newsList = posts.map { NewsListModel(it) }

                    newsList.map { item -> item.forListImageURL = media.find { m -> m.postLink == item.forListNewsURL }?.linkRaw }

                    newsListData.postValue(newsList)
                }
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
    }
}