package at.ict4d.ict4dnews.utils

/**
 * copied from https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150
 **/
class OnceEvent<out T>(private val content: T) {
    private var hasEventHandled = false

    fun getContentIfNotHandled(): T? {
        return if (hasEventHandled) {
            null
        } else {
            hasEventHandled = true
            content
        }
    }

    fun peekContent(): T = content
}