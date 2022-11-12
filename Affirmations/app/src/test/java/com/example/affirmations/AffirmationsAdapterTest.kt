package com.example.affirmations

import com.example.affirmations.adapter.ItemAdapter
import com.example.affirmations.model.Affirmation
import org.junit.Assert.assertEquals
import org.junit.Test
import android.content.Context
import org.mockito.Mockito.mock

class AffirmationsAdapterTest {

    // Create a "mocked" instance of a Context
    // [mock()] is used for testing
    // [Context] is current state of the app. Correct import: [import android.content.Context]
    private val context = mock(Context::class.java)

    /**
     * Check that the size of the adapter is the size of the list that was passed to the adapter.
     */
    @Test
    fun adapter_size() {

        // Load dataset
        val data = listOf(
            Affirmation(R.string.affirmation1, R.drawable.image1),
            Affirmation(R.string.affirmation2, R.drawable.image2)
        )

        // Create an instance of adapter with mocked context
        val adapter = ItemAdapter(context, data)

        // Check that adapter's getItemCount() method matches the size of the data list
        assertEquals("ItemAdapter is not the correct size", data.size, adapter.itemCount)

    }
}