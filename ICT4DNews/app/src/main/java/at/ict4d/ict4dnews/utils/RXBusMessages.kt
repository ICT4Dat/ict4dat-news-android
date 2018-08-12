package at.ict4d.ict4dnews.utils

import android.support.annotation.StringRes

class ServerErrorMessage(@StringRes val message: Int, val throwable: Throwable?)

class NewsRefreshDoneMessage

class BlogsRefreshDoneMessage