package com.mancave.pixabay.core.repository

import androidx.paging.PagingData
import com.mancave.pixabay.core.model.Image
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
  suspend fun find(query: String): Flow<PagingData<Image>>
}
