package at.ict4d.ict4dnews.server.utils

/**
 * A generic class that holds a value with its loading status.
 */
data class Resource<out T>(val status: Status, val data: T?, val throwable: Throwable?, val responseCode: Int?) {
    companion object {
        fun <T> success(
            data: T?,
            responseCode: Int?,
        ): Resource<T> {
            return Resource(
                Status.SUCCESS,
                data,
                null,
                responseCode,
            )
        }

        fun <T> error(
            throwable: Throwable,
            data: T?,
            responseCode: Int?,
        ): Resource<T> {
            return Resource(
                Status.ERROR,
                data,
                throwable,
                responseCode,
            )
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(
                Status.LOADING,
                data,
                null,
                null,
            )
        }
    }
}
