package kz.divtech.odyssey.rotation.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Ticket
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Trip
import java.io.File

object TicketDownloadUtil {
    fun downloadAllActiveTickets(trips: List<Trip>){
        trips.forEach { trip ->
            trip.segments.forEach { segment ->
                segment.ticket?.let { ticket ->
                    if(!ticket.getFile().exists()){
                        ticket.download()
                    }
                    if(!ticket.getFileFromDownloadsFolder().exists()){
                        ticket.downloadToExternalPublicFolder()
                    }
                }
            }
        }
    }

    fun Ticket.download(): Long {
        val pdfUrl = this.ticket_url
        val downloadManager = App.appContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(pdfUrl))
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
        request.setDestinationInExternalFilesDir(App.appContext, null, this.getFileName())
        return downloadManager.enqueue(request)
    }


    private fun Ticket.downloadToExternalPublicFolder(): Long {
        val pdfUrl = this.ticket_url
        val downloadManager = App.appContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(pdfUrl))
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
        request.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                App.appContext.getString(R.string.app_name) + File.separator +
                        this.getFileName())
        return downloadManager.enqueue(request)
    }

    fun Ticket.getFile(): File {
        return File(App.appContext.getExternalFilesDir(null), this.getFileName())
    }

    private fun Ticket.getFileFromDownloadsFolder(): File{
        return Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS +
                File.separator + App.appContext.getString(R.string.app_name) +
                File.separator + this.getFileName())
    }

    private fun Ticket.getFileName(): String{
        return "${this.id} ${this.dep_station_name} - ${this.arr_station_name}.pdf"
    }

}