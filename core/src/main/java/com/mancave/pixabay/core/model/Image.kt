package com.mancave.pixabay.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    @SerialName("hits") val images: List<Image> = emptyList()
)

@Parcelize
@Serializable
data class Image(
    // webformatURL image just look better than previewURL
    @SerialName("webformatURL") val previewImage: String,
    @SerialName("largeImageURL") val largeImage: String,
    @SerialName("user") val user: String,
    @SerialName("tags") val tags: String,
    @SerialName("likes") val likes: Int,
    @SerialName("downloads") val downloads: Int,
    @SerialName("comments") val comments: Int
): Parcelable
