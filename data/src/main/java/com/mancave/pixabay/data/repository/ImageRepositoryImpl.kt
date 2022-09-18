package com.mancave.pixabay.data.repository

import com.mancave.pixabay.core.datasource.PixabyApi
import com.mancave.pixabay.core.model.SearchResponse
import com.mancave.pixabay.core.repository.ImageRepository

class ImageRepositoryImpl(
    private val api: PixabyApi,
    private val apiKey: String,
): ImageRepository {

    override suspend fun find(query: String): Result<SearchResponse> = api
        .requestAsResult {
            it.search(
                key = apiKey,
                query = query,
                imageType = "photo"
            )
        }
}
