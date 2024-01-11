package kz.divtech.odyssey.rotation.ui.trips.refund.application.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.databinding.ItemTicketBinding
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Segment

class TicketAdapter : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {
    private val oldTicketList = mutableListOf<Segment>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val binding = ItemTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TicketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        holder.binding.ticket = oldTicketList[position].ticket
        holder.binding.ticketCB.isChecked = true
        holder.binding.ticketCB.isClickable = false
    }

    fun setTicketList(newTicketList: List<Segment>){
        oldTicketList.clear()
        oldTicketList.addAll(newTicketList)
        notifyDataSetChanged()
    }

    override fun getItemCount() = oldTicketList.size

    inner class TicketViewHolder(val binding: ItemTicketBinding): RecyclerView.ViewHolder(binding.root)

}