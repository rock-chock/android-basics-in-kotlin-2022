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
package com.example.forage.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.forage.BaseApplication
import com.example.forage.R
import com.example.forage.databinding.FragmentForageableListBinding
import com.example.forage.ui.adapter.ForageableListAdapter
import com.example.forage.ui.viewmodel.ForageableViewModel
import com.example.forage.ui.viewmodel.ForageableViewModelFactory

/**
 * A fragment to view the list of [Forageable]s stored in the database.
 * Clicking on a [Forageable] list item launches the [ForageableDetailFragment].
 * Clicking the [FloatingActionButton] launched the [AddForageableFragment]
 */
class ForageableListFragment : Fragment() {

    // Create a view model to take an instance of ForageableViewModelFactory.
    // Use by delegate to hand off the property initialization to the activityViewModels class.
    // The factory takes an instance of the Database retrieved from BaseApplication.
    private val viewModel: ForageableViewModel by activityViewModels {
        ForageableViewModelFactory(
            (activity?.application as BaseApplication).database.forageableDao()
        )
    }

    private var _binding: FragmentForageableListBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentForageableListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the new adapter property using ForageableListAdapter{}
        // Pass in the forageable id to navigation args.
        // Assign the returned NavDirections object to action.
        // Retrieve a NavController instance using this.findNavController() and call navigate() on it passing in the action
        // This lambda is used as a clickListener (see ForagebleListAdapter.clickListener parameter used at onBindViewHolder)
        val adapter = ForageableListAdapter { forageable ->
            val action = ForageableListFragmentDirections
                .actionForageableListFragmentToForageableDetailFragment(forageable.id)
            findNavController().navigate(action)
        }

        // Attach an observer on the allItems to listen for the data changes.
        // Call submitList() on the adapter and pass in the new list.
        // This will update the RecyclerView with the new items on the list.
        viewModel.allForageables.observe(this.viewLifecycleOwner) { forageables ->
            forageables.let {
                adapter.submitList(it)
            }

        }


        binding.apply {
            recyclerView.adapter = adapter
            addForageableFab.setOnClickListener {
                findNavController().navigate(
                    R.id.action_forageableListFragment_to_addForageableFragment
                )
            }
        }
    }
}
