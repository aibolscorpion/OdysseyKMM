package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kz.divtech.odyssey.rotation.databinding.ItemTicketNameBinding
import kz.divtech.odyssey.rotation.domain.model.trips.Ticket

class MyItemRecyclerViewAdapter(): RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {
    private val oldTicketList = mutableListOf<Ticket>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTicketNameBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.ticket = oldTicketList[position]
    }

    override fun getItemCount(): Int = oldTicketList.size

    fun setTicketList(newTicketList: List<Ticket>){
        oldTicketList.clear()
        oldTicketList.addAll(newTicketList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemTicketNameBinding) : RecyclerView.ViewHolder(binding.root)

}