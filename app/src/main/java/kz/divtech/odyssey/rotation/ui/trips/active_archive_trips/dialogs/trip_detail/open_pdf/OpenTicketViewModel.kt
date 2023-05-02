package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail.open_pdf

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.ViewModel
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Ticket
import java.io.File

class OpenTicketViewModel : ViewModel(){
    val fileMap = mutableMapOf<Long, File>()
    val ticketMap = mutableMapOf<Long, Ticket>()
    var downloadIdList = mutableListOf<Long>()

    fun downloadTicketByUrl(ticket: Ticket){
        val pdfUrl = ticket.ticket_url
        val downloadManager = App.appContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(pdfUrl))
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, getFilePath(ticket))
        val downloadId = downloadManager.enqueue(request)
        downloadIdList.add(downloadId)
        fileMap[downloadId] = getFileByTicket(ticket)
        ticketMap[downloadId] = ticket
    }

    fun getFileByTicket(ticket: Ticket): File{
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS
                + File.separator + getFilePath(ticket))
    }

    private fun getFilePath(ticket: Ticket): String{
        val fileNameWithExtension = "${ticket.id} " +
                "${ticket.dep_station_name} - ${ticket.arr_station_name}" +
                Config.ticketFileExtension
        return App.appContext.getString(R.string.app_name) + File.separator + fileNameWithExtension
    }


}