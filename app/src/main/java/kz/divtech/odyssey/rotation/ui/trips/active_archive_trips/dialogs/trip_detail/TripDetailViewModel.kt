package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.domain.model.trips.Trip

class TripDetailViewModel(val app: Application): AndroidViewModel(app) {
    var cityAndTimeInWay: ObservableField<String> = ObservableField("")

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


}