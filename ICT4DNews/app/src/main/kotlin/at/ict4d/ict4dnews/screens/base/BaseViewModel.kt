package at.ict4d.ict4dnews.screens.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {

    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()
    var isRefreshing: MutableLiveData<Boolean> = MutableLiveData()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}