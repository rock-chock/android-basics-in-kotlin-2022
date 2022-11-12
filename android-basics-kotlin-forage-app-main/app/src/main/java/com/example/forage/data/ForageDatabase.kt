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

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.forage.model.Forageable

/**
 * Room database to persist data for the Forage app.
 * This database stores a [Forageable] entity
 */

@Database(entities = [Forageable:: class], version = 1, exportSchema = false)
// Abstract class because we want to inherit methods and properties from RoomDatabase class without overriding all of them
abstract class ForageDatabase : RoomDatabase() {
    // An abstract fun that returns ForageableDao (interface which provides access to read/write operations)
    // Used to let database class know about DAO interface and to skip instantiation
    abstract fun forageableDao(): ForageableDao

    // Companion object for accessing database create/get method
    companion object {
        // @Volatile to make the changes visible immediately to other threads
        // Keep reference to the nullable instance of database
        @Volatile
        private var INSTANCE: ForageDatabase? = null

        // Create new or return an existing database
        fun getDatabase(context: Context): ForageDatabase {
            // Return INSTANCE variable or if INSTANCE is null, initialize it inside a synchronized{} block.
            // Synchronized block means that only one thread of execution at a time can enter this block of code
            return INSTANCE ?: synchronized(this) {

                // Database builder
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ForageDatabase::class.java,
                    "forage_database"
                )
                    // Migration strategy: destroy and rebuild the database, which means that the data is lost.
                    .fallbackToDestructiveMigration()
                    .build()

                return instance
            }
        }

    }

}
