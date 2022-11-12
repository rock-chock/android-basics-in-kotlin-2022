package com.example.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.data.Item
import com.example.inventory.data.getFormattedPrice
import com.example.inventory.databinding.ItemListItemBinding


/**
 * [ListAdapter] implementation for the recyclerview.
 */

// Pass in a function called onItemClicked() as a constructor parameter that takes in an Item object as parameter.
// ItemListAdapter extends ListAdapter. ListAdapter's parameters are Item and ItemListAdapter.ItemViewHolder
// Add the constructor parameter DiffCallback; the ListAdapter will use this to figure out what changed in the list.
class ItemListAdapter(
    private val onItemClicked: (Item) -> Unit) :
    ListAdapter<Item, ItemListAdapter.ItemViewHolder>(DiffCallback) {

    /*
    The onCreateViewHolder() method returns a new ViewHolder when RecyclerView needs one.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
    // Create a new View, inflate it from the item_list_item.xml layout file using the auto generated binding class, ItemListItemBinding.
        return ItemViewHolder(
             ItemListItemBinding.inflate(
                 LayoutInflater.from(
                     parent.context
                 )
             )
        )
    }

    // Get the current item using the method getItem(), passing the position.
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        // Use ItemViewHolder's bind() method
        holder.bind(current)
    }

    // Define the ItemViewHolder class, extend it from RecyclerView.ViewHolder.
    // Override the bind() function, pass in the Item object.
    class ItemViewHolder(private var binding: ItemListItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        // Bind the itemName TextView to item.itemName.
        // Get the price in currency format using the getFormattedPrice() extension function, and bind it to the itemPrice TextView.
        // Convert the quantityInStock value to String, and bind it to the itemQuantity TextView.
        fun bind(item: Item) {
            binding.apply {
                itemName.text = item.itemName
                itemPrice.text = item.getFormattedPrice()
                itemQuantity.text = item.quantityInStock.toString()
            }
        }
    }

    // Inside the companion object, define a val of the type DiffUtil.ItemCallback<Item>() called DiffCallback.
    // Override the required methods areItemsTheSame() and areContentsTheSame(), and define them.
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Item>() {

            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.itemName == newItem.itemName
            }

        }
    }
}

