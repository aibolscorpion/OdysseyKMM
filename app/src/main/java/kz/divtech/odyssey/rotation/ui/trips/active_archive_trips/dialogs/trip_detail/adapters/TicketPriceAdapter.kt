package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.databinding.ItemTicketPriceBinding
import kz.divtech.odyssey.rotation.domain.model.trips.Ticket

class TicketPriceAdapter : RecyclerView.Adapter<TicketPriceAdapter.TicketPriceViewHolder>() {
    private val ticketList = mutableListOf<Ticket>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketPriceViewHolder {
        val binding = ItemTicketPriceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TicketPriceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TicketPriceViewHolder, position: Int) {
        holder.bind(ticketList[position])
    }

    override fun getItemCount(): Int = ticketList.size

    fun setTicketPriceList(priceList : List<Ticket>){
        ticketList.clear()
        ticketList.addAll(priceList)
        notifyDataSetChanged()
    }

    inner class TicketPriceViewHolder(val binding: ItemTicketPriceBinding) : RecyclerView.ViewHolder(binding.root){
        val context: Context = binding.root.context

        fun bind(ticket: Ticket){
            binding.departureDestinationTV.text = App.appContext.getString(
                R.string.dash_sign_btw_two_text, ticket.dep_station_name, ticket.arr_station_name)
            binding.ticketPrice.text = context.getString(R.string.ticket_price, ticket.sum)
        }
    }
}