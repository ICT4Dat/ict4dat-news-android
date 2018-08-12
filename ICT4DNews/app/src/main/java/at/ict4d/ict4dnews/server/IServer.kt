package at.ict4d.ict4dnews.server

import io.reactivex.disposables.Disposable
import org.threeten.bp.LocalDateTime

interface IServer {

    fun loadICT4DatRSSFeed(): Disposable

    fun loadICT4DatJsonFeed(newsAfterDateTime: LocalDateTime): Disposable

    fun loadBlogs(): Disposable
}