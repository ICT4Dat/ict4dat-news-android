package at.ict4d.ict4dnews.dagger.modules

import at.ict4d.ict4dnews.BuildConfig
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.models.FeedType
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.server.ApiICT4DatNews
import at.ict4d.ict4dnews.server.ApiJsonSelfHostedWPService
import at.ict4d.ict4dnews.server.ApiRSSService
import at.ict4d.ict4dnews.server.IServer
import at.ict4d.ict4dnews.server.Server
import at.ict4d.ict4dnews.utils.GsonFeedTypeDeserializer
import at.ict4d.ict4dnews.utils.GsonLocalDateTimeDeserializer
import at.ict4d.ict4dnews.utils.RxEventBus
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.threeten.bp.LocalDateTime
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.io.File
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApiServiceModule {

    @Provides
    @Singleton
    fun provideRSSApiService(okHttpClient: OkHttpClient): ApiRSSService {
        return Retrofit.Builder()
            .baseUrl("http://will.be.overritten.com")
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build().create(ApiRSSService::class.java)
    }

    @Provides
    @Singleton
    fun provideJsonWordpressApiService(okHttpClient: OkHttpClient, gson: Gson): ApiJsonSelfHostedWPService {
        return Retrofit.Builder()
            .baseUrl("http://will.be.overritten.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build().create(ApiJsonSelfHostedWPService::class.java)
    }

    @Provides
    @Singleton
    fun provideICT4DatNewsApiService(okHttpClient: OkHttpClient, gson: Gson): ApiICT4DatNews {
        return Retrofit.Builder()
            .baseUrl("http://www.ict4d.at/ict4dnews/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build().create(ApiICT4DatNews::class.java)
    }

    @Provides
    @Reusable
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, GsonLocalDateTimeDeserializer())
            .registerTypeAdapter(FeedType::class.java, GsonFeedTypeDeserializer())
            .create()
    }

    @Provides
    @Reusable
    fun provideServer(
        apiRSSService: ApiRSSService,
        apiJsonSelfHostedWPService: ApiJsonSelfHostedWPService,
        apiICT4DatNews: ApiICT4DatNews,
        persistenceManager: IPersistenceManager,
        eventBus: RxEventBus
    ): IServer = Server(apiRSSService, apiJsonSelfHostedWPService, apiICT4DatNews, persistenceManager, eventBus)

    @Provides
    @Singleton
    fun provideOkHttpClient(application: ICT4DNewsApplication): OkHttpClient {

        val cacheDir = File(application.cacheDir, UUID.randomUUID().toString())
        // 10 MiB cache
        val cache = Cache(cacheDir, 10 * 1024 * 1024)

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        val builder = OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(StethoInterceptor())
        }

        return builder.build()
    }
}