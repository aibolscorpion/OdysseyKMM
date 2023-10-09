package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.common.App
import kz.divtech.odyssey.rotation.databinding.ItemSegmentShortBinding
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Segment
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Trip
import kz.divtech.odyssey.rotation.common.utils.Utils.getRefundSegmentStatus

class SegmentAdapter : Adapter<SegmentAdapter.SegmentViewHolder>() {
    private val listOfSegments = ArrayList<Segment>()
    var trip: Trip? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SegmentViewHolder {
        val binding = ItemSegmentShortBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SegmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SegmentViewHolder, position: Int) {
        val segment = listOfSegments[position]
        holder.binding.noSegmentInfoLL.isVisible = (segment.train == null)
        holder.binding.segmentInfoCL.isVisible = (segment.train != null)
        if(segment.train != null){
            holder.binding.segment = segment
            holder.binding.refundSegmentStatus = getRefundSegmentStatus(trip?.refund_applications!!, listOfSegments[position].id)
        }else{
            holder.binding.segmentStationsTV.text = App.appContext.getString(
                R.string.dash_sign_btw_two_text, segment.dep_station_name, segment.arr_station_name)
        }
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