package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips

import androidx.recyclerview.widget.DiffUtil
import kz.divtech.odyssey.rotation.domain.model.trips.Trip

class TripsDiffCallBack(private val newList: List<Trip>, private val oldList: List<Trip>) : DiffUtil.Callback(){
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
         newList[newItemPosition].id == oldList[oldItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
         newList[newItemPosition] == oldList[oldItemPosition]
}