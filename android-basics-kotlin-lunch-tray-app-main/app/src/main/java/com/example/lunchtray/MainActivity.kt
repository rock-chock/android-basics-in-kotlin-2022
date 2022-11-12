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
package com.example.lunchtray

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.lunchtray.databinding.ActivityMainBinding
import com.example.lunchtray.model.OrderViewModel

class MainActivity : AppCompatActivity() {

    // Binding object instance corresponding to the activity_main.xml layout
    // when the view hierarchy is attached to the fragment.
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get a reference to the Fragment Container View with id nav_host_fragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        // Get reference to it's navController
        navController = navHostFragment.navController

        // Set up action bar with nav controller
        // This will:
        // - set titles from destination's labels in nav_graph
        // - display Up button whenever we're not on top level destination
        setupActionBarWithNavController(navController)
    }

    // Set up the up button via navController that is available as MainActivity class property and set in onCreate
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}