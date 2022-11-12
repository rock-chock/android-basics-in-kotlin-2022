package com.example.inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import kotlinx.coroutines.launch


class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {

    // Call getItems() on itemDao and assign it to allItems. The getItems() function returns a Flow.
    // To consume the data as a LiveData value, use the asLiveData() function.
    val allItems: LiveData<List<Item>> = itemDao.getItems().asLiveData()

    // Take in an Item object and add the data to the database in a non-blocking way
    private fun insertItem(item: Item) {
        // To interact with the database off the main thread, start a coroutine and call the DAO method within it.
        // The ViewModelScope is an extension property to the ViewModel class that automatically cancels its child coroutines when the ViewModel is destroyed
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }

    // Take in three strings and return an Item instance
    private fun getNewItemEntry(
        itemName: String,
        itemPrice: String,
        itemCount: String
    ) : Item {
        return Item(
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
    }


    // Take in three strings for item details
    fun addNewItem(
        itemName: String,
        itemPrice: String,
        itemCount:String
    ) {
        val newItem = getNewItemEntry(itemName, itemPrice, itemCount)
        insertItem(newItem)
    }


    // Verify user input before adding or updating the entity in the database
    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }

    // Call getItem() on the itemDao, passing in the parameter id.
    // To consume the returned Flow value as LiveData call asLiveData() function and use this as the return of retrieveItem() function.
    fun retrieveItem(id:Int): LiveData<Item> {
        return itemDao.getItem(id).asLiveData()
    }

    // Update item using update() method from the ItemDao class
    private fun updateItem(item: Item) {
        // Launch a coroutine to use suspend method update()
        viewModelScope.launch {
            itemDao.update(item)
        }

    }

    // Decrease quantity of item when Sell button is clicked
    fun sellItem(item:Item) {
        if (item.quantityInStock > 0) {
            // Use copy() function that is available for all data classes to copy an object and change some of its properties
            // Decrease item's quantity by 1
            val newItem = item.copy(quantityInStock = item.quantityInStock - 1)
            // Call updateItem() to apply changes to the item in the database
            updateItem(newItem)
        }
    }

    // Check if quantity is greater than 0
    fun isStockAvailable(item: Item): Boolean {
        return (item.quantityInStock > 0)
    }


    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemDao.delete(item)
        }
    }

    // Return an item instance from input fields
    // Used when editing details of existing item
    private fun getUpdatedItemEntry(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemCount: String
    ): Item {
        return Item(
            id = itemId,
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
    }

    // Get updatedItem from UI and use private updateItem() function that uses coroutine for calling suspend function
    fun updateItem(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemCount: String
    ) {
        val updatedItem = getUpdatedItemEntry(itemId, itemName, itemPrice, itemCount)
        updateItem(updatedItem)
    }

}


// InventoryViewModelFactory class for instantiating the InventoryViewModel instance
class InventoryViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {

    // Take any class type as an argument and return a ViewModel object
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        // Check if the modelClass is the same as the InventoryViewModel class and return an instance of it.
        // Otherwise, throw an exception.
        if(modelClass.isAssignableFrom(InventoryViewModel:: class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}