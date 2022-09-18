package com.mancave.pixabay.data.repository

import com.mancave.pixabay.core.datasource.PixabyApi
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

inline fun <T> PixabyApi.requestAsResult(
    requestExecutor: (PixabyApi) -> Response<T>
): Result<T> {
    return try {
        val response = requestExecutor(this)
        val body = response.body()
        if (body != null) {
            Result.success(body)
        } else Result.failure(
            if (!response.isSuccessful) {
                HttpException(response)
            } else IllegalStateException("Body expected")
        )
    } catch (serializationException: SerializationException) {
        Result.failure(serializationException)
    } catch (networkException: IOException) {
        Result.failure(networkException)
    }
}
