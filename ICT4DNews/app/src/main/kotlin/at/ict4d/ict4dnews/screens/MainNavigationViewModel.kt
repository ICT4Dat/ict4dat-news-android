package at.ict4d.ict4dnews.screens

import at.ict4d.ict4dnews.screens.base.BaseViewModel
import at.ict4d.ict4dnews.background.UpdateNewsServiceHandler
import javax.inject.Inject

class MainNavigationViewModel @Inject constructor(
    updateNewsServiceHandler: UpdateNewsServiceHandler
) : BaseViewModel() {

    init {
        updateNewsServiceHandler.requestToRegisterService()
    }
}