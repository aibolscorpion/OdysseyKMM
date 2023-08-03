package kz.divtech.odyssey.rotation.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Ticket
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Trip
import java.io.File

object DownloadUtil {
    fun downloadAllActiveTickets(trips: List<Trip>){
        trips.forEach { trip ->
            trip.segments.forEach { segment ->
                segment.ticket?.let { ticket ->
                    downloadTicket(ticket)
                }
            }
        }
    }

    fun downloadTicket(ticket: Ticket): Long?{
        var downloadId : Long? = null
        if(!getFileByTicket(ticket).exists()){
            val pdfUrl = ticket.ticket_url
            val downloadManager = App.appContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val request = DownloadManager.Request(Uri.parse(pdfUrl))
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, getFilePath(ticket))
            downloadId = downloadManager.enqueue(request)
        }
        return downloadId
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