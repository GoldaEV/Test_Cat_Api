package com.golda.test.cats.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.golda.test.cats.data.room.model.CatData

@Database(
    entities = [CatData::class], version = 1, exportSchema = false
)

abstract class FavoriteCatDatabase : RoomDatabase() {
    abstract val countriesDao: FavoriteDao
}