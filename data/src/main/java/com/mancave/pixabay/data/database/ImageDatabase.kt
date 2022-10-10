package com.mancave.pixabay.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mancave.pixabay.core.model.Image
import com.mancave.pixabay.data.dao.ImageDao

@Database(entities = [Image::class], version = 1)
abstract class ImageDatabase : RoomDatabase() {
  abstract fun imageDao(): ImageDao
}
