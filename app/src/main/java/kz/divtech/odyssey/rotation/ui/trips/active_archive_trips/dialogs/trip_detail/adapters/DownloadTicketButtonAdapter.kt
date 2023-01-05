package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.ItemDownloadTicketBinding
import kz.divtech.odyssey.rotation.domain.model.trips.Ticket


class DownloadTicketButtonAdapter(val downloadInterface: DownloadInterface) : RecyclerView.Adapter<DownloadTicketButtonAdapter.TicketViewHolder>() {
    private val listOfTickets = mutableListOf<Ticket>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val binding = ItemDownloadTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TicketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        holder.bind(listOfTickets[position])
    }

    override fun getItemCount() = listOfTickets.size

    fun setTicketList(ticketList: List<Ticket>?){
        listOfTickets.clear()
        ticketList?.let { listOfTickets.addAll(it) }
        notifyDataSetChanged()
    }

    inner class TicketViewHolder(val binding: ItemDownloadTicketBinding) : RecyclerView.ViewHolder(binding.root){
        private val context = binding.root.context
        fun bind(ticket: Ticket){
            binding.root.setOnClickListener{
                downloadInterface.onTicketClicked(ticket)
            }
            binding.ticketName = context.getString(R.string.download_ticket,
                ticket.dep_station_name, ticket.arr_station_name)
        }

    }

    interface DownloadInterface{
        fun onTicketClicked(currentTicket: Ticket)
    }

}