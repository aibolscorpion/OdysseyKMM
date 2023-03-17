package kz.divtech.odyssey.rotation.ui.trips.refund.create_application

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.databinding.ItemTicketBinding
import kz.divtech.odyssey.rotation.domain.model.trips.Ticket

class TicketAdapter : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {
    private val ticketList = mutableListOf<Ticket>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val binding = ItemTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TicketViewHolder(binding)
    }

    fun setTicketList(newTicketList: List<Ticket>){
        ticketList.clear()
        ticketList.addAll(newTicketList)
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        holder.binding.root.setOnClickListener {
            holder.binding.ticketCB.isChecked = !holder.binding.ticketCB.isChecked
        }
    }

    override fun getItemCount() = 10

    inner class TicketViewHolder(val binding: ItemTicketBinding)
        : RecyclerView.ViewHolder(binding.root)

}