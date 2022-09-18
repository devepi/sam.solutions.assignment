package com.mancave.pixabay.core.repository

import com.mancave.pixabay.core.model.SearchResponse

interface ImageRepository {
    suspend fun find(query: String): Result<SearchResponse>
}
