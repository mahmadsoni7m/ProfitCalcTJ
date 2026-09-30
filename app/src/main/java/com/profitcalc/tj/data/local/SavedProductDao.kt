package com.profitcalc.tj.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedProductDao {
    @Query("SELECT * FROM saved_products ORDER BY name ASC")
    fun observeAll(): Flow<List<SavedProductEntity>>

    @Insert
    suspend fun insert(entity: SavedProductEntity): Long

    @Update
    suspend fun update(entity: SavedProductEntity)

    @Delete
    suspend fun delete(entity: SavedProductEntity)
}
