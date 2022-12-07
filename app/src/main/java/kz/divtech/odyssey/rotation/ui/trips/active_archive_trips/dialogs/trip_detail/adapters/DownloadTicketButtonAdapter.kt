package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail.adapters

import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.databinding.ItemDownloadTicketBinding
import kz.divtech.odyssey.rotation.domain.model.trips.Ticket

class DownloadTicketButtonAdapter : RecyclerView.Adapter<DownloadTicketButtonAdapter.TicketViewHolder>() {
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

    class TicketViewHolder(val binding: ItemDownloadTicketBinding) : RecyclerView.ViewHolder(binding.root){
        var currentTicket :Ticket? = null

        init {
            binding.downloadTicketButton.setOnClickListener{
                val pdfUrl = currentTicket?.ticket_url

                val downloadManager = App.appContext.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                val request = DownloadManager.Request(Uri.parse(pdfUrl))

                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

                downloadManager.enqueue(request)
            }
        }

        fun bind(ticket: Ticket){
            currentTicket = ticket
            binding.ticketName = App.appContext.getString(R.string.download_ticket,
                currentTicket!!.dep_station_name, currentTicket!!.arr_station_name)
        }

    }

}