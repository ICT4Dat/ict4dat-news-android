package at.ict4d.ict4dnews.server

import io.reactivex.disposables.Disposable

interface IServer {

    fun loadICT4DatRSSFeed(): Disposable

    fun loadICT4DatJsonFeed(): Disposable
}