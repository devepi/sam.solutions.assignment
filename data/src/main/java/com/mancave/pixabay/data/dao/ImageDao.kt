package com.mancave.pixabay.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mancave.pixabay.core.model.Image

@Dao
interface ImageDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(images: List<Image>)

  @Query("SELECT * FROM images ORDER BY imageId")
  fun pagingSource(): PagingSource<Int, Image>

  @Query("DELETE FROM images")
  suspend fun clearAll()
}
