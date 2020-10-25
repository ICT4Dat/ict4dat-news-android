package at.ict4d.ict4dnews.di.modules

import at.ict4d.ict4dnews.server.repositories.AuthorRepository
import at.ict4d.ict4dnews.server.repositories.BlogsRepository
import at.ict4d.ict4dnews.server.repositories.NewsRepository
import org.koin.dsl.module

val repositoryModule = module {

    single { BlogsRepository(get(), get()) }

    single { NewsRepository(get(), get(), get(), get(), get(), get(), get()) }

    single { AuthorRepository(get()) }
}
