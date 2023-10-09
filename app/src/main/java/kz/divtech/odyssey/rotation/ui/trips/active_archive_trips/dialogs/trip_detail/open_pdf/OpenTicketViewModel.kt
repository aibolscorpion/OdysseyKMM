package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail.open_pdf

import androidx.lifecycle.ViewModel
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Ticket
import kz.divtech.odyssey.rotation.common.utils.TicketDownloadUtil.download
import kz.divtech.odyssey.rotation.common.utils.TicketDownloadUtil.getFile
import java.io.File

class OpenTicketViewModel : ViewModel(){
    private val fileMap = mutableMapOf<Long, File?>()
    private val ticketMap = mutableMapOf<Long, Ticket>()
    private val downloadIdList = mutableListOf<Long>()

    val files: Map<Long, File?> get() = fileMap
    val tickets: Map<Long, Ticket> get() = ticketMap
    val downloadIds: List<Long> get() = downloadIdList
    fun downloadTicketByURL(ticket: Ticket){
        ticket.download().let { downloadId ->
            downloadIdList.add(downloadId)
            fileMap[downloadId]= ticket.getFile()
            ticketMap[downloadId] = ticket
        }
    }

}