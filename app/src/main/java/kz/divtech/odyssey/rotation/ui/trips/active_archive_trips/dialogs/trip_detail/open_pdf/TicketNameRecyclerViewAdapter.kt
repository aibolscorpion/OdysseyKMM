package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail.open_pdf

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.ItemTicketNameBinding
import kz.divtech.odyssey.rotation.common.utils.TicketDownloadUtil.getFile
import kz.divtech.odyssey.rotation.common.utils.LocalDateTimeUtils.toDateString
import kz.divtech.odyssey.rotation.common.utils.Utils.getAppLocale
import kz.divtech.odyssey.rotation.common.utils.Utils.getFileSize
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Ticket

class TicketNameRecyclerViewAdapter(private val downloadInterface: DownloadInterface,
        val context: Context): RecyclerView.Adapter<TicketNameRecyclerViewAdapter.ViewHolder>() {
    private val oldTicketList = mutableListOf<Ticket>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTicketNameBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val ticket = oldTicketList[position]
       holder.binding.ticket = ticket

       val file = ticket.getFile()
        if(file.exists()){
            holder.binding.fileIV.setImageResource(R.drawable.icon_pdf_file)
            holder.binding.ticketFileDescTV.text = context.getString(
                R.string.file_extension_modified_date_and_size, file.extension,
                file.lastModified().toDateString(context.getAppLocale()), file.getFileSize())
        }else{
            holder.binding.fileIV.setImageResource(R.drawable.icon_download)
            holder.binding.ticketFileDescTV.text = context.getString(R.string.click_to_download)
        }

        holder.binding.root.setOnClickListener{
            downloadInterface.onTicketClicked(ticket)
        }
    }

    override fun getItemCount(): Int = oldTicketList.size

    fun setTicketList(newTicketList: List<Ticket>){
        val diffCallback = TicketCallBack(oldTicketList, newTicketList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        oldTicketList.clear()
        oldTicketList.addAll(newTicketList)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(val binding: ItemTicketNameBinding) : RecyclerView.ViewHolder(binding.root)

}

interface DownloadInterface{
    fun onTicketClicked(currentTicket: Ticket)
}
