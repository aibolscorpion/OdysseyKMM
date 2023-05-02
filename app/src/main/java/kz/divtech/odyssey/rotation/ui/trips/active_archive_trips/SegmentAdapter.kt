package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kz.divtech.odyssey.rotation.databinding.ItemSegmentShortBinding
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Segment
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Trip
import kz.divtech.odyssey.rotation.utils.Utils.getRefundSegmentStatus

class SegmentAdapter : Adapter<SegmentAdapter.SegmentViewHolder>() {
    private val listOfSegments = ArrayList<Segment>()
    var trip: Trip? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SegmentViewHolder {
        val binding = ItemSegmentShortBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SegmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SegmentViewHolder, position: Int) {
        holder.binding.segments = listOfSegments[position]
        holder.binding.refundSegmentStatus = getRefundSegmentStatus(trip?.refund_applications!!, listOfSegments[position].id)
    }

    override fun getItemCount() = listOfSegments.size

    fun setSegmentList(trip: Trip?){
        this.trip = trip
        listOfSegments.clear()
        trip?.segments?.let { listOfSegments.addAll(it) }
        notifyDataSetChanged()
    }

    class SegmentViewHolder(val binding: ItemSegmentShortBinding) : ViewHolder(binding.root)

}