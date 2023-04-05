package kz.divtech.odyssey.rotation.domain.model.trips

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Segment(
    var active_process: String?,
    val application_id: Int,
    val arr_station: String?,
    val arr_station_code: String?,
    val arr_station_name: String?,
    val closed_reason: String?,
    val created_at: String?,
    val deleted_at:  String?,
    val dep_station: String?,
    val dep_station_code: String?,
    val dep_station_name: String?,
    val icon: String?,
    val id: Int,
    var status: String?,
    val ticket: Ticket?,
    val ticket_id: Int?,
    val train: Train?,
    val updated_at: String?,
    val watcher_time_limit: String?
) : Parcelable
