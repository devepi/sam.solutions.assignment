package com.mancave.pixabay.core.datasource

import com.mancave.pixabay.core.model.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabyApi {

    @GET("/api")
    suspend fun search(
        @Query("key") key: String,
        @Query("q") query: String,
        @Query("image_type") imageType: String
    ): Response<SearchResponse>

}
