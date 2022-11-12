package com.example.affirmations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.affirmations.adapter.ItemAdapter
import com.example.affirmations.data.Datasource

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load a list of affirmations
        val myDataset = Datasource().loadAffirmations()

        // Find a reference to RecyclerView within the layout (stored in activity_main.xml)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        // Make flexible recyclerView from activity_main.xml use the ItemAdapter class with the dataset
        // to fill the recyclerView with a list of TextViews that display affirmations
        // [context] - context of this activity
        recyclerView.adapter = ItemAdapter(this, myDataset)
        // Set fixed size for the layout.
        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true)
    }
}
