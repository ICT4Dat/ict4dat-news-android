package at.ict4d.ict4dnews.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.ict4d.ict4dnews.background.UpdateNewsServiceHandler
import at.ict4d.ict4dnews.persistence.sharedpreferences.SharedPrefs
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainNavigationViewModel(
    private val updateNewsServiceHandler: UpdateNewsServiceHandler,
    private val sharedPrefs: SharedPrefs
) : ViewModel() {

    fun watchAutomaticNewsUpdates() {
        viewModelScope.launch {
            sharedPrefs.isAutomaticNewsUpdateEnabled
                .asFlow()
                .collect { isEnabled ->
                    if (isEnabled) {
                        updateNewsServiceHandler.requestToRegisterService()
                    } else {
                        updateNewsServiceHandler.cancelTask()
                    }
                }
        }
    }
}
