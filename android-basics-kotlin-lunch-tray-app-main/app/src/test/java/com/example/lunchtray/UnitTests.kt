package com.example.lunchtray

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Transformations
import com.example.lunchtray.model.OrderViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*
import java.text.NumberFormat

class UnitTests {

    // Specify that LiveData objects should not call the main thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Make the variable accessible for all tests
    private lateinit var viewModel: OrderViewModel

    // Use this to ensure that our instantTaskExecutorRule is defined before the viewModel is defined
    @Before
    fun setup() {
        viewModel = OrderViewModel()

        // Observe live data to capture changes, otherwise nothing is updated
        viewModel.subtotal.observeForever {}
        viewModel.total.observeForever {}
    }

    @Test
    fun initializationEntreeNull() {
        assertEquals(null, viewModel.entree.value)
    }

    @Test
    fun initializationSubtotalZero() {
        assertEquals("$0.00", viewModel.subtotal.value)
    }


    @Test
    fun setEntreeCauliflowerSubtotal() {
        viewModel.setEntree("cauliflower")
        assertEquals("$7.00", viewModel.subtotal.value)
    }

    @Test
    fun setEntreeAndChooseAnother() {
        viewModel.setEntree("cauliflower")
        viewModel.setEntree("chili")
        assertEquals("$4.00", viewModel.subtotal.value)
    }

    @Test
    fun setEntreeAndSide() {
        viewModel.setEntree("cauliflower")
        viewModel.setSide("salad")
        assertEquals("$9.50", viewModel.subtotal.value)
    }

    @Test
    fun setEntreeChooseNewEntreeSetSide() {
        viewModel.setEntree("cauliflower")
        viewModel.setEntree("chili")
        viewModel.setSide("salad")
        assertEquals("$6.50", viewModel.subtotal.value)
    }

    @Test
    fun resetOrder() {
        viewModel.setEntree("cauliflower")
        viewModel.setSide("salad")
        viewModel.resetOrder()
        assertEquals("$0.00", viewModel.subtotal.value)
    }

    @Test
    fun calculateTotalWithTaxes() {
        viewModel.setEntree("cauliflower")
        viewModel.setSide("salad")
        val total = (7.0 + 2.5) + ((7.0 + 2.5)*0.08)
        val formattedTotal = NumberFormat.getCurrencyInstance().format(total)
        assertEquals(formattedTotal, viewModel.total.value )
    }


}