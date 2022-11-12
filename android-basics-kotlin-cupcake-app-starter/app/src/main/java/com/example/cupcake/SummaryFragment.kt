/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.cupcake

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.databinding.FragmentSummaryBinding
import com.example.cupcake.model.OrderViewModel

/**
 * [SummaryFragment] contains a summary of the order details with a button to share the order
 * via another app.
 */
class SummaryFragment : Fragment() {

    // Initialize a shared view model as an activityViewModels via using the property delegate.
    // Property delegation handoff the getter-setter responsibility to a different class.
    // Using activityViewModels() instead of viewModels() allows to share the data within the activity, not only within the fragment
    private val sharedViewModel: OrderViewModel by activityViewModels()

    // Binding object instance corresponding to the fragment_summary.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment.
    private var binding: FragmentSummaryBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentSummaryBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            // Set the lifecycle owner on the binding object
            // By setting the lifecycle owner, the app will be able to observe LiveData objects.
            // [viewLifecycleOwner] here is SummaryFragment
            lifecycleOwner = viewLifecycleOwner

            // Bind the variable set in fragment layout [viewModel] with the shared view model instance (OrderViewModel.kt) for Data Binding
            viewModel = sharedViewModel

            // Bind layout variable with this fragment to be able to set Binding Listener for onClick event
            summaryFragment = this@SummaryFragment
        }
    }

    fun cancelOrder() {
        sharedViewModel.resetOrder()
        findNavController().navigate(R.id.action_summaryFragment_to_startFragment)
    }


    /**
     * Submit the order by sharing out the order details to another app via an implicit intent.
     */
    fun sendOrder() {

        // Use elvis ?: operator to use 0 if the quantity is null
        val numberOfCupcakes = sharedViewModel.quantity.value ?: 0

        // Prepare email body with order details
        val orderSummary = getString(
            R.string.order_details,
            // Get a plural to create the formatted cupcakes string
            resources.getQuantityString(R.plurals.cupcakes, numberOfCupcakes, numberOfCupcakes),
            sharedViewModel.flavor.value.toString(),
            sharedViewModel.date.value.toString(),
            sharedViewModel.price.value.toString()
        )

        // Prepare intent before starting
        // Use putExtra() to pass data
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.new_cupcake_order))
            putExtra(Intent.EXTRA_TEXT, orderSummary)
        }

        // Check if there is an app that can handle this action, otherwise the app might crash
        if (activity?.packageManager?.resolveActivity(intent, 0) != null) {
            startActivity(intent)
        }
    }


    /**
     * This fragment lifecycle method is called when the view hierarchy associated with the fragment
     * is being removed. As a result, clear out the binding object.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}