package com.mancave.pixabay.data.repository

import androidx.paging.*
import androidx.room.withTransaction
import com.mancave.pixabay.core.datasource.PixabyApi
import com.mancave.pixabay.core.model.Image
import com.mancave.pixabay.core.repository.ImageRepository
import com.mancave.pixabay.data.database.ImageDatabase
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ImageRepositoryImpl(
  private val api: PixabyApi,
  private val apiKey: String,
  private val database: ImageDatabase
) : ImageRepository {

  private val imageDao = database.imageDao()

  override suspend fun find(query: String): Flow<PagingData<Image>> {
    return Pager(
      config = PagingConfig(
        initialLoadSize = 20,
        pageSize = 20,
        maxSize = 100,
        enablePlaceholders = false
      ),
      remoteMediator = SearchRemoteMediator(
        api = api,
        apiKey = apiKey,
        query = query,
        database = database
      )
    ) {
      imageDao.pagingSource()
    }.flow
  }
}

@ExperimentalPagingApi
class SearchRemoteMediator(
  private val api: PixabyApi,
  private val apiKey: String,
  private val query: String,
  private val database: ImageDatabase
) : RemoteMediator<Int, Image>() {

  private val imageDao = database.imageDao()
  private var page: Int = STARTING_PAGE_INDEX

  override suspend fun initialize(): InitializeAction {
    return InitializeAction.LAUNCH_INITIAL_REFRESH
  }

  override suspend fun load(
    loadType: LoadType,
    state: PagingState<Int, Image>
  ): MediatorResult {
    return try {

      page = when (loadType) {
        LoadType.REFRESH -> STARTING_PAGE_INDEX
        LoadType.PREPEND -> return MediatorResult.Success(
          endOfPaginationReached = true
        )
        LoadType.APPEND -> page.plus(1)
      }

      val response = api.search(
        key = apiKey,
        query = query,
        page = page,
        pageSize = state.config.pageSize
      )

      database.withTransaction {
        if (loadType == LoadType.REFRESH) {
          imageDao.clearAll()
        }
        imageDao.insertAll(response.images)
      }

      MediatorResult.Success(
        endOfPaginationReached = response.images.isEmpty()
      )
    } catch (e: IOException) {
      MediatorResult.Error(e)
    } catch (e: HttpException) {
      MediatorResult.Error(e)
    }
  }

  companion object {
    private const val STARTING_PAGE_INDEX = 1
  }
}