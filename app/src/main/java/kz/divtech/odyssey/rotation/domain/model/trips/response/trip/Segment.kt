package kz.divtech.odyssey.rotation.domain.model.trips.response.trip

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Segment(
    val id: Int,
    val dep_station_code: String?,
    val dep_station_name: String,
    val arr_station_code: String?,
    val arr_station_name: String,
    var status: String,
    var active_process: String?,
    val closed_reason: String?,
    val created_at: String,
    val updated_at: String,
    val watcher_time_limit: String?,
    val watcher_action: String?,
    val ticket: Ticket?,
    val train: Train?
) : Parcelable
