package at.ict4d.ict4dnews.extensions

import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.conflate
import timber.log.Timber

@ExperimentalCoroutinesApi
fun <E> SendChannel<E>.safeOffer(value: E) = !isClosedForSend && try {
    trySend(value).isSuccess
} catch (e: CancellationException) {
    false
}

@ExperimentalCoroutinesApi
fun SearchView.queryTextChanges() = callbackFlow<String> {
    val listener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(newText: String): Boolean {
            safeOffer(newText)
            return true
        }

        override fun onQueryTextSubmit(query: String): Boolean = false
    }
    setOnQueryTextListener(listener)

    awaitClose { setOnQueryTextListener(null) }
}.conflate()
    .catch { e -> Timber.w(e) }
