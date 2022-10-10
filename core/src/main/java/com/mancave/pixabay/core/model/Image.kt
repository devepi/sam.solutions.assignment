package com.mancave.pixabay.core.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
  @SerialName("hits") val images: List<Image> = emptyList()
)

@Parcelize
@Serializable
@Entity(tableName = "images")
data class Image(

  @kotlinx.serialization.Transient
  @PrimaryKey(autoGenerate = true)
  val imageId: Long? = null,

  @SerialName("id")
  val id: Long,

  @SerialName("webformatURL")
  @ColumnInfo(name = "web_format_url")
  val previewImage: String,

  @SerialName("largeImageURL")
  @ColumnInfo(name = "large_image_url")
  val largeImage: String,

  @SerialName("user")
  val user: String,

  @SerialName("tags")
  val tags: String,

  @SerialName("likes")
  val likes: Int,

  @SerialName("views")
  val views: Int,

  @SerialName("downloads")
  val downloads: Int,

  @SerialName("comments")
  val comments: Int,

  ) : Parcelable
