package com.example.inventory.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Provides access to read/write operations on the item table.
 * Used by the view models to format the query results for use in the UI.
 */
@Dao
interface ItemDao {
    // Make the function a suspend function, so that this function can be called from a coroutine.
    // The argument OnConflict tells the Room what to do in case of a conflict. The OnConflictStrategy.
    // IGNORE strategy ignores a new item if it's primary key is already in the database.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    // Use the colon notation in the query to reference arguments in the function.
    // Use Flow to ensure we get notified whenever the data in the database changes.
    @Query("SELECT * FROM item WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    @Query("SELECT * FROM item ORDER BY name")
    fun getItems():Flow<List<Item>>
}