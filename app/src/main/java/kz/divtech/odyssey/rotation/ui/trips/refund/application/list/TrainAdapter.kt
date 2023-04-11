package kz.divtech.odyssey.rotation.ui.trips.refund.application.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.databinding.ItemTrainBinding
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.domain.model.trips.Segment
import kz.divtech.odyssey.rotation.domain.model.trips.refund.applications.RefundSegment

class TrainAdapter(val trip: Trip) : RecyclerView.Adapter<TrainAdapter.TicketViewHolder>() {
    private val oldSegmentList = mutableListOf<RefundSegment>()

    fun setSegmentList(newSegmentList: List<RefundSegment>){
        oldSegmentList.clear()
        oldSegmentList.addAll(newSegmentList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val binding = ItemTrainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TicketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        holder.binding.train = getSegmentById(oldSegmentList[position].segment_id)?.train
    }

    override fun getItemCount() = oldSegmentList.size

    private fun getSegmentById(segmentId: Int): Segment?{
        var segment: Segment? = null
        trip.segments?.forEach {
            if(it.id == segmentId){
                segment = it
            }
        }
        return segment
    }

    inner class TicketViewHolder(val binding: ItemTrainBinding): RecyclerView.ViewHolder(binding.root)

}