package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.ItemTicketPriceBinding
import kz.divtech.odyssey.rotation.domain.model.trips.Ticket
import timber.log.Timber

class TicketPriceAdapter : RecyclerView.Adapter<TicketPriceAdapter.TicketPriceViewHolder>() {
    private val listOfPrices = mutableListOf<Ticket>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketPriceViewHolder {
        val binding = ItemTicketPriceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TicketPriceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TicketPriceViewHolder, position: Int) {
        Timber.i("price = ${listOfPrices[position].sum}, position = $position")
        holder.bind(listOfPrices[position], position)
    }

    override fun getItemCount(): Int = listOfPrices.size

    fun setTicketPriceList(priceList : List<Ticket>){
        listOfPrices.clear()
        listOfPrices.addAll(priceList)
        notifyDataSetChanged()
    }

    class TicketPriceViewHolder(val binding: ItemTicketPriceBinding) : RecyclerView.ViewHolder(binding.root){
        val context: Context = binding.root.context

        fun bind(ticket: Ticket, position: Int){
            binding.ticketPosition.text = context.getString(R.string.ticket_position_number, position+1)
            binding.ticketPrice.text = context.getString(R.string.ticket_price, ticket.sum)
        }
    }
}