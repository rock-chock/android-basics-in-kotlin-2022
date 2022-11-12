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
package com.example.forage.ui.viewmodel

import androidx.lifecycle.*
import com.example.forage.data.ForageableDao
import com.example.forage.model.Forageable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

/**
 * Shared [ViewModel] to provide data to the [ForageableListFragment], [ForageableDetailFragment],
 * and [AddForageableFragment] and allow for interaction the the [ForageableDao]
 */

// Pass a ForageableDao value as a parameter to the view model constructor
class ForageableViewModel(
    private val forageableDao: ForageableDao
): ViewModel() {

    // Create a property to set to a list of all forageables from the DAO
    // To consume the Flow data as LiveData, use the asLiveData() function.
    val allForageables: LiveData<List<Forageable>> = forageableDao.getForageables().asLiveData()


    // Create method that takes id: Long as a parameter and retrieve a Forageable from the
    // database by id via the DAO.
    fun getForageable(id: Long): LiveData<Forageable> {
        return forageableDao.getForageable(id).asLiveData()
    }


    // Take in a Forageable object and add the data to the database using coroutine
    fun addForageable(
        name: String,
        address: String,
        inSeason: Boolean,
        notes: String
    ) {
        // Create a new Forageable instance using passed properties
        val forageable = Forageable(
            name = name,
            address = address,
            inSeason = inSeason,
            notes = notes
        )

        // Launch a coroutine and call the DAO method to add a Forageable to the database within it
        viewModelScope.launch(Dispatchers.IO) {
            forageableDao.insert(forageable)
        }
    }


    fun updateForageable(
        id: Long,
        name: String,
        address: String,
        inSeason: Boolean,
        notes: String
    ) {
        val forageable = Forageable(
            id = id,
            name = name,
            address = address,
            inSeason = inSeason,
            notes = notes
        )
        // Call the DAO method to update a forageable to the database here
        viewModelScope.launch(Dispatchers.IO) {
            forageableDao.update(forageable)
        }
    }


    fun deleteForageable(forageable: Forageable) {
        // Call the DAO method to delete a forageable to the database here
        viewModelScope.launch(Dispatchers.IO) {
            forageableDao.delete(forageable)
        }
    }


    fun isValidEntry(name: String, address: String): Boolean {
        return name.isNotBlank() && address.isNotBlank()
    }
}


// A view model factory that takes a ForageableDao as a property and creates a ForageableViewModel
class ForageableViewModelFactory (
    private val forageableDao: ForageableDao
) : ViewModelProvider.Factory {

    // Take any class type as an argument and return a ViewModel object
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        // Check if the modelClass is the same as the ForageableViewModel class and return an instance of it.
        // Otherwise, throw an exception.
        if(modelClass.isAssignableFrom(ForageableViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ForageableViewModel(forageableDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}