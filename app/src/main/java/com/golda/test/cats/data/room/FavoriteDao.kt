package com.golda.test.cats.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.golda.test.cats.data.room.model.CatData
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM CatFavorites")
    fun getAll(): Flow<List<CatData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(cat: CatData): Long

    @Query("DELETE FROM CatFavorites WHERE idString = :idString")
    fun deleteFavorite(idString: String): Int
}