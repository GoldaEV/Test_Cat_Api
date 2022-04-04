package com.golda.test.cats.di

import android.app.Application
import androidx.room.Room
import coil.ImageLoader
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import com.golda.test.cats.BuildConfig
import com.golda.test.cats.Dispatchers
import com.golda.test.cats.data.network.ApiService
import com.golda.test.cats.data.room.FavoriteCatDatabase
import com.golda.test.cats.data.room.FavoriteDao
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object AppModule {

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json(Json.Default) {
            ignoreUnknownKeys = true
        }
    }
    @Singleton
    @Provides
    fun provideFavoriteCatDatabase(application: Application): FavoriteCatDatabase {
        return Room.databaseBuilder(application, FavoriteCatDatabase::class.java, "cats")
            .fallbackToDestructiveMigration()
            .build()
    }
    @Singleton
    @Provides
    fun provideFavoriteDao(database: FavoriteCatDatabase): FavoriteDao {
        return  database.countriesDao
    }


    @Singleton
    @Provides
    fun provideNewsService(json: Json): ApiService {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val httpClient = OkHttpClient.Builder()
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(httpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideDispatchers(): Dispatchers {
        return Dispatchers.Default
    }

    @Singleton
    @Provides
    fun provideImageLoader(application: Application): ImageLoader {
        return ImageLoader(application)
    }
}