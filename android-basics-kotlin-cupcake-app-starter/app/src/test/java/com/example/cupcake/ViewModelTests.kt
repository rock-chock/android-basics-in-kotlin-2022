package com.example.cupcake

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.cupcake.model.OrderViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class ViewModelTests {

    // Specify that LiveData objects should not call the main thread.
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Make the variable accessible for all tests
    private lateinit var viewModel: OrderViewModel

    // Use this to ensure that our instantTaskExecutorRule is defined before the viewModel is defined
    @Before
    fun setup() {
        viewModel = OrderViewModel()
    }

    @Test
    fun quantity_twelve_cupcakes() {
        viewModel.setQuantity(12)
        assertEquals(12, viewModel.quantity.value)
    }

    @Test
    fun price_twelve_cupcakes() {
        viewModel.setQuantity(12)
        // Observe live data to capture changes, otherwise the price is not updated
        viewModel.price.observeForever {}
        // 12*$2 + 3 = 27
        assertEquals("$27.00", viewModel.price.value)
    }

}