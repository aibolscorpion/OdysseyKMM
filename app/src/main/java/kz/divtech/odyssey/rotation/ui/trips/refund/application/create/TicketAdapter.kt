package kz.divtech.odyssey.rotation.ui.trips.refund.application.create

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.databinding.ItemTicketBinding
import kz.divtech.odyssey.rotation.domain.model.trips.Segment

class TicketAdapter(private val checkListener: OnItemCheckListener) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {
    private val segmentList = mutableListOf<Segment>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val binding = ItemTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TicketViewHolder(binding)
    }

    fun setTicketList(newSegmentList: List<Segment>){
        segmentList.clear()
        segmentList.addAll(newSegmentList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val segment = segmentList[position]
        val checkBox = holder.binding.ticketCB
        holder.binding.ticket = segment.ticket
        holder.binding.root.setOnClickListener {
            checkBox.isChecked = !checkBox.isChecked
            if(checkBox.isChecked){
                checkListener.onItemCheck(segment.id)
            }else{
                checkListener.onItemUncheck(segment.id)
            }
        }
    }

    override fun getItemCount() = segmentList.size

    inner class TicketViewHolder(val binding: ItemTicketBinding)
        : RecyclerView.ViewHolder(binding.root)

    interface OnItemCheckListener{
        fun onItemCheck(segmentId: Int)
        fun onItemUncheck(segmentId: Int)
    }

}
