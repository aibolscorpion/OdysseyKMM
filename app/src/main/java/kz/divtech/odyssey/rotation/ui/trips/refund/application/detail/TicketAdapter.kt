package kz.divtech.odyssey.rotation.ui.trips.refund.application.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.databinding.ItemTicketBinding
import kz.divtech.odyssey.rotation.domain.model.trips.Segment
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.domain.model.trips.refund.applications.RefundSegment

class TicketAdapter(val trip: Trip) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {
    private val oldTicketList = mutableListOf<RefundSegment>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val binding = ItemTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TicketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        holder.binding.ticket = getSegmentById(oldTicketList[position].segment_id)?.ticket
        holder.binding.ticketCB.isChecked = true
        holder.binding.ticketCB.isClickable = false
    }

    fun setTicketList(newTicketList: List<RefundSegment>){
        oldTicketList.clear()
        oldTicketList.addAll(newTicketList)
        notifyDataSetChanged()
    }

    private fun getSegmentById(segmentId: Int): Segment?{
        var segment: Segment? = null
        trip.segments?.forEach {
            if(it.id == segmentId){
                segment = it
            }
        }
        return segment
    }

    override fun getItemCount() = oldTicketList.size

    inner class TicketViewHolder(val binding: ItemTicketBinding): RecyclerView.ViewHolder(binding.root)

}