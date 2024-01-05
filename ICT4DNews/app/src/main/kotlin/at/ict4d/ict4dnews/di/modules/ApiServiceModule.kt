package at.ict4d.ict4dnews.di.modules

import at.ict4d.ict4dnews.BuildConfig
import at.ict4d.ict4dnews.models.FeedType
import at.ict4d.ict4dnews.server.api.ApiICT4DatNews
import at.ict4d.ict4dnews.server.api.ApiJsonSelfHostedWPService
import at.ict4d.ict4dnews.server.api.ApiRssService
import at.ict4d.ict4dnews.utils.GsonFeedTypeDeserializer
import at.ict4d.ict4dnews.utils.GsonLocalDateTimeDeserializer
import com.google.android.gms.security.ProviderInstaller
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import timber.log.Timber
import java.io.File
import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.TimeUnit

val apiServiceModule =
    module {
        single<ApiRssService> {
            Retrofit.Builder()
                .baseUrl("http://will.be.overritten.com")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(get<OkHttpClient>())
                .build().create(ApiRssService::class.java)
        }

        single<ApiJsonSelfHostedWPService> {
            Retrofit.Builder()
                .baseUrl("http://will.be.overritten.com")
                .addConverterFactory(GsonConverterFactory.create(get<Gson>()))
                .client(get<OkHttpClient>())
                .build().create(ApiJsonSelfHostedWPService::class.java)
        }

        single<ApiICT4DatNews> {
            Retrofit.Builder()
                .baseUrl("http://www.ict4d.at/ict4dnews/")
                .addConverterFactory(GsonConverterFactory.create(get<Gson>()))
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
            val builder =
                OkHttpClient.Builder()
                    .cache(get<Cache>())
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(get<HttpLoggingInterceptor>())

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
    }
