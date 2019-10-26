package at.ict4d.ict4dnews.di.modules

import at.ict4d.ict4dnews.BuildConfig
import at.ict4d.ict4dnews.models.FeedType
import at.ict4d.ict4dnews.server.ApiICT4DatNews
import at.ict4d.ict4dnews.server.ApiJsonSelfHostedWPService
import at.ict4d.ict4dnews.server.ApiRSSService
import at.ict4d.ict4dnews.server.IServer
import at.ict4d.ict4dnews.server.Server
import at.ict4d.ict4dnews.utils.GsonFeedTypeDeserializer
import at.ict4d.ict4dnews.utils.GsonLocalDateTimeDeserializer
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.android.gms.security.ProviderInstaller
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import java.util.UUID
import java.util.concurrent.TimeUnit
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.threeten.bp.LocalDateTime
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import timber.log.Timber

val apiServiceModule = module {

    single<ApiRSSService> {
        Retrofit.Builder()
            .baseUrl("http://will.be.overritten.com")
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(get<OkHttpClient>())
            .build().create(ApiRSSService::class.java)
    }

    single<ApiJsonSelfHostedWPService> {
        Retrofit.Builder()
            .baseUrl("http://will.be.overritten.com")
            .addConverterFactory(GsonConverterFactory.create(get<Gson>()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(get<OkHttpClient>())
            .build().create(ApiJsonSelfHostedWPService::class.java)
    }

    single<ApiICT4DatNews> {
        Retrofit.Builder()
            .baseUrl("http://www.ict4d.at/ict4dnews/")
            .addConverterFactory(GsonConverterFactory.create(get<Gson>()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(get<OkHttpClient>())
            .build().create(ApiICT4DatNews::class.java)
    }

    single<Gson> {
        GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, GsonLocalDateTimeDeserializer())
            .registerTypeAdapter(FeedType::class.java, GsonFeedTypeDeserializer())
            .create()
    }

    single {
        val builder = OkHttpClient.Builder()
            .cache(get<Cache>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(get<HttpLoggingInterceptor>())

        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(StethoInterceptor())
        }

        try {
            ProviderInstaller.installIfNeeded(androidContext())
        } catch (e: Exception) {
            Timber.e("SecurityException: Google Play Services not available.")
        }

        builder.build()
    }

    single {
        // 10 MiB cache
        Cache(File(androidApplication().cacheDir, UUID.randomUUID().toString()), 10 * 1024 * 1024)
    }

    single {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        httpLoggingInterceptor
    }

    factory<IServer> {
        Server(
            apiRSSService = get(),
            apiJsonSelfHostedWPService = get(),
            apiICT4DatNews = get(),
            persistenceManager = get(),
            rxEventBus = get()
        )
    }
}
