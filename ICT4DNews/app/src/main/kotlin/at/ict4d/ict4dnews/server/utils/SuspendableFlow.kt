package at.ict4d.ict4dnews.server.utils

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.Response
import timber.log.Timber
import java.net.UnknownHostException

@ExperimentalCoroutinesApi
fun <ResultType, RemoteResultType> resultFlow(
    databaseQuery: () -> Flow<ResultType>,
    networkCall: suspend () -> Response<RemoteResultType>,
    saveCallResult: suspend (RemoteResultType) -> Unit,
    emitFutureDatabaseUpdates: Boolean = true
) = flow<Resource<ResultType>> {

    val dbSource = databaseQuery.invoke()

    try {
        emit(Resource.loading(dbSource.first()))

        val response = networkCall.invoke()

        val resourceFlow = when (val apiResponse = ApiResponse.create(response)) {
            is ApiSuccessResponse -> {
                saveCallResult(apiResponse.body)
                dbSource.map { Resource.success(it, apiResponse.responseCode) }
            }
            is ApiEmptyResponse -> {
                dbSource.map { Resource.success(it, apiResponse.responseCode) }
            }
            is ApiErrorResponse -> {
                dbSource.map {
                    Resource.error(
                        apiResponse.throwable,
                        it,
                        apiResponse.responseCode
                    )
                }
            }
        }

        if (emitFutureDatabaseUpdates) {
            emitAll(resourceFlow)
        } else {
            emit(resourceFlow.first())
        }
    } catch (e: Exception) {

        Timber.w(e, "Error in resultFlow")

        if (e !is CancellationException) {
            if (emitFutureDatabaseUpdates) {
                emitAll(dbSource.map {
                    Resource.error(e, it, null)
                })
            } else {
                emit(Resource.error(e, dbSource.first(), null))
            }
        }
    }
}.flowOn(Dispatchers.IO) // run async on IO thread
    .distinctUntilChanged() // only emit new data and distinct until it changed
