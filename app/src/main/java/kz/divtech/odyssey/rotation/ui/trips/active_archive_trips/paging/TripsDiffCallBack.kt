package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.paging

import androidx.recyclerview.widget.DiffUtil
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Trip

class TripsDiffCallBack : DiffUtil.ItemCallback<Trip>(){
    override fun areItemsTheSame(oldItem: Trip, newItem: Trip): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Trip, newItem: Trip): Boolean {
        return oldItem == newItem
    }

}