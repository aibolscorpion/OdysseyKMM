package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.domain.model.trips.Ticket
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import java.io.File

class TripDetailViewModel(val app: Application): AndroidViewModel(app) {
    var cityAndTimeInWay: ObservableField<String> = ObservableField("")
    var currentTicket: Ticket? = null

    private val _fileMutableLiveData = MutableLiveData<File>()
    val fileLiveData: LiveData<File> = _fileMutableLiveData

    private val map = mutableMapOf<Long, File>()

    var downloadId: Long = 0

    fun setCityAndTotalTimeInWay(trip: Trip){
        var totalMinutes = 0
        trip.segments?.forEach { segment ->
            totalMinutes += segment.train?.in_way_minutes!!
        }
        val hours = totalMinutes/60
        val minutes = totalMinutes%60

        val totalTimeInWay = app.getString(R.string.total_time_in_way, trip.end_station, hours, minutes)

        cityAndTimeInWay.set(totalTimeInWay)
    }


    fun openFileIfExists(currentTicket: Ticket) {
        this.currentTicket = currentTicket
        val file = getFile()
        if(file.exists()){
            _fileMutableLiveData.value = file
        }else{
            downloadTicket()
        }
    }

    private fun downloadTicket(){
        val pdfUrl = currentTicket?.ticket_url!!
        val downloadManager = App.appContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(pdfUrl))
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, getFilePath())
        downloadId = downloadManager.enqueue(request)
        map[downloadId] = getFile()
    }

    fun getFileById(downloadId: Long): File{
        return map[downloadId]!!
    }

    private fun getFile(): File{
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS
                + File.separator + getFilePath())
    }

    private fun getFilePath(): String{
        val fileNameWithExtension = "${currentTicket?.id} " +
                "${currentTicket?.dep_station_name} - ${currentTicket?.arr_station_name}" +
                Config.ticketFileExtension
        return App.appContext.getString(R.string.app_name) + File.separator + fileNameWithExtension
    }

}