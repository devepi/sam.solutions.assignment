package com.mancave.pixabay.app.search.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mancave.pixabay.core.datasource.PixabyApi
import com.mancave.pixabay.core.repository.ImageRepository
import com.mancave.pixabay.data.database.ImageDatabase
import com.mancave.pixabay.data.repository.ImageRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
@OptIn(ExperimentalSerializationApi::class)
internal object SearchModule {

  private const val pixabayUrl = "https://pixabay.com/api/"
  private const val pixabayApiKey = "29978836-4c051161c578d8ade01a18a85"

  private val json = Json {
    coerceInputValues = true
    ignoreUnknownKeys = true
    isLenient = true
  }

  @Provides
  @ViewModelScoped
  fun providePixabyApi(
    client: OkHttpClient,
    converterFactory: Converter.Factory
  ): PixabyApi = Retrofit.Builder()
    .baseUrl(pixabayUrl)
    .client(client)
    .addConverterFactory(converterFactory)
    .build()
    .create(PixabyApi::class.java)

  @Provides
  @ViewModelScoped
  fun provideConverterFactory(): Converter.Factory = json
    .asConverterFactory("application/json".toMediaType())

  @Provides
  @ViewModelScoped
  fun provideHttpClient(): OkHttpClient = OkHttpClient()
    .newBuilder()
    .addInterceptor(
      HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
      }
    )
    .build()

  @Provides
  @ViewModelScoped
  fun provideImageRepository(
    pixabyApi: PixabyApi,
    database: ImageDatabase,
  ): ImageRepository = ImageRepositoryImpl(
    api = pixabyApi,
    apiKey = pixabayApiKey,
    database = database
  )
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Singleton
  @Provides
  fun provideImageDatabase(
    @ApplicationContext app: Context
  ) = Room.databaseBuilder(
    app,
    ImageDatabase::class.java,
    "pixabay-database"
  ).build()

  @Singleton
  @Provides
  fun provideImageDao(
    db: ImageDatabase
  ) = db.imageDao()
}
