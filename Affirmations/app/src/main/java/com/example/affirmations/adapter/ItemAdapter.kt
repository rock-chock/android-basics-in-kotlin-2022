package com.example.affirmations.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.affirmations.R
import com.example.affirmations.model.Affirmation

/**
 * Adapter for the [RecyclerView] in MainActivity. Displays [Affirmation] data object.
 */
// ItemAdapter takes a dataset as a parameter and a context.
// It extracts an instance from the dataset and turns it into a list item view.
// [context] stores information about app environment, including hierarchy, string resources.
// [dataset] is private because the dataset will only be used within this class.
class ItemAdapter(
    private val context: Context,
    private val dataset: List<Affirmation>
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    // A ViewHolder holds a reference to the empty individual view
    // (text view [item_title] from list_item.xml)
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_title)
        val imageView: ImageView = view.findViewById(R.id.item_image)
    }

    // Methods inherited from an abstract class RecyclerView.Adapter

    /**
     * Create new view holders for the RecyclerView (invoked by the layout manager)
     */
    // Take an XML as an input.
    // Returns an ItemViewHolder object (= empty text view) within the hierarchy of view objects.
    // [parent] is the view group(RecyclerView) that the new list item view will be attached to as a child.
    // [viewType] specifies which view type should be recycled (e.g. if there are multiple item view types)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        // [LayoutInflater] knows how to inflate an XML layout into a hierarchy of view objects.
        // [adapterLayout] holds a reference to the list item view (RecyclerView)
        // [inflate] takes an XML piece, converts it to a view and attaches to the specified parent view group
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ItemAdapter.ItemViewHolder, position: Int) {
        // Get the item at the given position in the dataset.
        // [item] is an affirmation object from the dataset (affirmation from list of affirmation)
        val item = dataset[position]
        // Set the text of the ItemViewHolder's TextView to display the Affirmation string for this item.
        // stringResourceId is integer, so we need to get the actual string value connected to the id
        holder.textView.text = context.resources.getString(item.stringResourceId)
        // Set the affirmation item's imageResourceId onto the ImageView of the list item view
        holder.imageView.setImageResource(item.imageResourceId)
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    override fun getItemCount() = dataset.size

}