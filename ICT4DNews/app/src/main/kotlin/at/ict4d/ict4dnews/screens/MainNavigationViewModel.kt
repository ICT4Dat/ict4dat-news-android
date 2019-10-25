package at.ict4d.ict4dnews.screens

import at.ict4d.ict4dnews.background.UpdateNewsServiceHandler
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.screens.base.BaseViewModel
import io.reactivex.schedulers.Schedulers

class MainNavigationViewModel(
    updateNewsServiceHandler: UpdateNewsServiceHandler,
    persistenceManager: IPersistenceManager
) : BaseViewModel() {

    init {
        compositeDisposable.add(persistenceManager.isAutomaticNewsUpdateEnabled().asObservable()
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe {
                if (it) {
                    updateNewsServiceHandler.requestToRegisterService()
                } else {
                    updateNewsServiceHandler.cancelTask()
                }
            })
    }
}