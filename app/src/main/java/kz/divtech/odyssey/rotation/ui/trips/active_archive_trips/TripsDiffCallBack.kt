package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips

import androidx.recyclerview.widget.DiffUtil
import kz.divtech.odyssey.rotation.domain.model.trips.Trip

class TripsDiffCallBack(private val oldList: List<Trip>, private val newList: List<Trip>) : DiffUtil.Callback(){
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}