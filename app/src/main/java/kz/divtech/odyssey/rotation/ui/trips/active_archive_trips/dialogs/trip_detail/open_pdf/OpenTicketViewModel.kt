package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail.open_pdf

import androidx.lifecycle.ViewModel
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Ticket
import kz.divtech.odyssey.rotation.utils.DownloadUtil.downloadTicket
import kz.divtech.odyssey.rotation.utils.DownloadUtil.getFileByTicket
import java.io.File

class OpenTicketViewModel : ViewModel(){
    val fileMap = mutableMapOf<Long, File>()
    val ticketMap = mutableMapOf<Long, Ticket>()
    var downloadIdList = mutableListOf<Long>()

    fun downloadTicketByUrl(ticket: Ticket){
        downloadTicket(ticket)?.let {
            downloadIdList.add(it)
            fileMap[it]= getFileByTicket(ticket)
            ticketMap[it] = ticket
        }
    }

}