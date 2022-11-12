package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val PRICE_PER_CUPCAKE = 2.00
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

class OrderViewModel : ViewModel() {

    // Add private mutable properties to updated them in this class
    // Add backing public  immutable properties to expose them outside of this class
    // Change properties' types to LiveData type so that these properties can be observable and UI can be updated when data changes
    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    private val _flavor = MutableLiveData<String>()
    val flavor: LiveData<String> = _flavor

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    // Apply price formatting onto double value
    // Transformations.map() takes a property and a lambda function
    // getCurrencyInstance() convert the double to local currency format
    private val _price = MutableLiveData<Double>()
    val price: LiveData<String> = Transformations.map(_price) {
        NumberFormat.getCurrencyInstance().format(it)
    }

    val dateOptions = getPickupOptions()


    init {
        // Set all properties to null
        resetOrder()
    }

    fun setQuantity(numberCupcakes: Int) {
        _quantity.value = numberCupcakes
        updatePrice()
    }

    fun setFlavor(desiredFlavor: String) {
        _flavor.value = desiredFlavor
    }

    fun setDate(pickupDate: String) {
        _date.value = pickupDate
        updatePrice()
    }

    fun hasNoFlavorSet(): Boolean {
        return _flavor.value.isNullOrEmpty()
    }

    private fun getPickupOptions(): List<String> {
        val options = mutableListOf<String>()
        // Create a formatter string
        // Locale object represents a specific geographical, political, or cultural region
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())

        // Get a Calendar instance
        val calendar = Calendar.getInstance()

        // Build up a list of dates starting with the current date and the following 3 dates
        // Format a date, add it to the list of date options, then increment the calendar by 1
        repeat(4) {
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }

        return options
    }

    // Reset the MutableLiveData properties in the view mode
    fun resetOrder() {
        _quantity.value = 0
        _flavor.value = ""
        _date.value = dateOptions[0]
        _price.value = 0.0
    }

    private fun updatePrice() {

        // Get pickup price: compare user's date and the first date in dateOptions list
        val pickupPrice = if (_date.value == dateOptions[0]) {
            PRICE_FOR_SAME_DAY_PICKUP
            } else 0.0

        // Calculate the total price
        _price.value = ((_quantity.value?: 0) * PRICE_PER_CUPCAKE) + pickupPrice
    }

}