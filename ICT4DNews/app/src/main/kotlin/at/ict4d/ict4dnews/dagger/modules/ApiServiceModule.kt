package at.ict4d.ict4dnews.dagger.modules

import at.ict4d.ict4dnews.BuildConfig
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.models.FeedType
import at.ict4d.ict4dnews.server.ApiICT4DatNews
import at.ict4d.ict4dnews.server.ApiJsonSelfHostedWPService
import at.ict4d.ict4dnews.server.ApiRSSService
import at.ict4d.ict4dnews.server.IServer
import at.ict4d.ict4dnews.server.Server
import at.ict4d.ict4dnews.utils.GsonFeedTypeDeserializer
import at.ict4d.ict4dnews.utils.GsonLocalDateTimeDeserializer
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
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

@Module
abstract class ApiServiceModule {

    @Binds
    @Reusable
    abstract fun provideServer(server: Server): IServer

    @Module
    companion object {

        @Provides
        @JvmStatic
        @Reusable
        fun provideRxJava2CallAdapterFactory() = RxJava2CallAdapterFactory.create()

        @Provides
        @JvmStatic
        @Reusable
        fun provideSimpleXmlConverterFactory() = SimpleXmlConverterFactory.create()

        @Provides
        @JvmStatic
        @Reusable
        fun provideGsonConverterFactory(gson: Gson) = GsonConverterFactory.create(gson)

        @Provides
        @JvmStatic
        fun provideRSSApiService(
            okHttpClient: OkHttpClient,
            xmlFactory: SimpleXmlConverterFactory,
            rxFactory: RxJava2CallAdapterFactory
        ): ApiRSSService {
            return Retrofit.Builder()
                .baseUrl("http://will.be.overritten.com")
                .addConverterFactory(xmlFactory)
                .addCallAdapterFactory(rxFactory)
                .client(okHttpClient)
                .build().create(ApiRSSService::class.java)
        }

        @Provides
        @JvmStatic
        fun provideJsonWordpressApiService(
            okHttpClient: OkHttpClient,
            gsonConverterFactory: GsonConverterFactory,
            rxFactory: RxJava2CallAdapterFactory
        ): ApiJsonSelfHostedWPService {
            return Retrofit.Builder()
                .baseUrl("http://will.be.overritten.com")
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxFactory)
                .client(okHttpClient)
                .build().create(ApiJsonSelfHostedWPService::class.java)
        }

        @Provides
        @JvmStatic
        fun provideICT4DatNewsApiService(
            okHttpClient: OkHttpClient,
            gsonConverterFactory: GsonConverterFactory,
            rxFactory: RxJava2CallAdapterFactory
        ): ApiICT4DatNews {
            return Retrofit.Builder()
                .baseUrl("http://www.ict4d.at/ict4dnews/")
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxFactory)
                .client(okHttpClient)
                .build().create(ApiICT4DatNews::class.java)
        }

        @Provides
        @JvmStatic
        fun provideGson(): Gson {
            return GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, GsonLocalDateTimeDeserializer())
                .registerTypeAdapter(FeedType::class.java, GsonFeedTypeDeserializer())
                .create()
        }

        @Provides
        @JvmStatic
        @Reusable
        fun provideOkHttpClient(cache: Cache, httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
            val builder = OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)

            if (BuildConfig.DEBUG) {
                builder.addNetworkInterceptor(StethoInterceptor())
            }

            return builder.build()
        }

        @Provides
        @JvmStatic
        fun provideCache(application: ICT4DNewsApplication): Cache {
            // 10 MiB cache
            return Cache(File(application.cacheDir, UUID.randomUUID().toString()), 10 * 1024 * 1024)
        }

        @Provides
        @JvmStatic
        @Reusable
        fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            } else {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
            }

            return httpLoggingInterceptor
        }
    }
}