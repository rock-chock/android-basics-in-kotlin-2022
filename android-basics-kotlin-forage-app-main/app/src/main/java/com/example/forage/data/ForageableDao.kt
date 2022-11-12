/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.forage.data

import androidx.room.*
import com.example.forage.model.Forageable
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for database interaction.
 * Provides access to read/write operations on the Forageable table.
 * Used by the view models to format the query results for use in the UI.
 */
@Dao
interface ForageableDao {

    // Retrieve all Forageables from the database
    // Used in a ViewModel's variable to show the dynamic list
    // Use the colon notation in the query to reference arguments in the function.
    // Use Flow to ensure we get notified whenever the data in the database changes.
    // No need to suspend because it returns LiveData which runs asynchronously using Room under the hood
    @Query("SELECT * FROM forageable ORDER BY name")
    fun getForageables(): Flow<List<Forageable>>

    @Query("SELECT * FROM forageable WHERE id =:id")
    fun getForageable(id: Long): Flow<Forageable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(forageable: Forageable)

    @Update
    suspend fun update(forageable: Forageable)

    @Delete
    suspend fun delete(forageable: Forageable)
}
