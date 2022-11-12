package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Room database to persist data for the Inventory app
@Database(entities = [Item:: class], version = 1, exportSchema = false)
abstract class ItemRoomDatabase : RoomDatabase() {
    // An abstract function that returns ItemDao (interface which provides access to read/write operations)
    abstract fun itemDao(): ItemDao

    // Allows access to the methods for creating or getting the database using the class name as the qualifier.
    companion object {
        // The value of a volatile variable will never be cached, and all writes and reads will be done to and from the main memory
        // It means that changes made by one thread to INSTANCE are visible to all other threads immediately.
        @Volatile
        // Declare a private nullable variable INSTANCE for the database and initialize it to null
        // INSTANCE variable keeps a reference to the database, when one has been created.
        private var INSTANCE: ItemRoomDatabase? = null

        fun getDatabase(context: Context): ItemRoomDatabase {
            // Return INSTANCE variable or if INSTANCE is null, initialize it inside a synchronized{} block.
            // Synchronized block means that only one thread of execution at a time can enter this block of code
            return INSTANCE ?: synchronized(this) {

                // Use database builder
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemRoomDatabase::class.java,
                    "item_database"
                )
                // Add the required migration strategy to the builder
                // Destroy and rebuild the database, which means that the data is lost.
                    .fallbackToDestructiveMigration()
                    .build()

                return instance
            }
        }
    }
}

