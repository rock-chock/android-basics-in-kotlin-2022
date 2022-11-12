package com.example.busschedule

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.busschedule.database.Schedule
import com.example.busschedule.databinding.BusStopItemBinding
import java.text.SimpleDateFormat
import java.util.*

/*
The class extends a generic ListAdapter that takes a list of Schedule objects and a BusStopViewHolder class for the UI.
For the BusStopViewHolder, you also pass in a DiffCallback type which you'll define soon.
The BusStopAdapter class itself also takes a parameter, onItemClicked().
 */
class BusStopAdapter(
    private val onItemClicked: (Schedule) -> Unit) :
    ListAdapter<Schedule, BusStopAdapter.BusStopViewHolder>(DiffCallback) {

    class BusStopViewHolder(private var binding: BusStopItemBinding): RecyclerView.ViewHolder(binding.root) {

        // Implement the bind() function to set stopNameTextView's text to the stop name and the arrivalTimeTextView's text to the formatted date.
        // SuppressLint indicates that Lint should ignore the specified warnings for the annotated element.
        @SuppressLint("SimpleDateFormat")
        fun bind(schedule: Schedule) {
            binding.stopNameTextView.text = schedule.stopName
            binding.arrivalTimeTextView.text = SimpleDateFormat(
                "h:mm a").format(
                    Date(schedule.arrivalTime.toLong() * 1000)
            )
        }
    }

    // Inflate the layout and set the onClickListener() to call onItemClicked() for the item at the current position.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusStopViewHolder {

        val viewHolder = BusStopViewHolder(
            BusStopItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }

        return viewHolder
    }


    // Get the current item using the method getItem(), passing the position.
    override fun onBindViewHolder(holder: BusStopViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    // Inside the companion object, define a val of the type DiffUtil.ItemCallback<Schedule>() called DiffCallback.
    // Override the required methods areItemsTheSame() and areContentsTheSame(), and define them.
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Schedule>() {
            override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
                return oldItem == newItem
            }
        }
    }

}