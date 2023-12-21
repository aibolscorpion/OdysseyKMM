package kz.divtech.odyssey.shared.domain.model.trips.response.trip

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Segment(
    val id: Int,
    @SerialName("dep_station_code")
    val depStationCode: String?,
    @SerialName("dep_station_name")
    val depStationName: String,
    @SerialName("arr_station_code")
    val arrStationCode: String?,
    @SerialName("arr_station_name")
    val arrStationName: String,
    var status: String,
    @SerialName("active_process")
    var activeProcess: String?,
    @SerialName("closed_reason")
    val closedReason: String?,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("watcher_time_limit")
    val watcherTimeLimit: String?,
    @SerialName("watcher_action")
    val watcherAction: String?,
    val ticket: Ticket?,
    val train: Train?
)
