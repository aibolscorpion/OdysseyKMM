package kz.divtech.odyssey.shared.domain.model.trips.response.trip

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Train(
    @SerialName("arr_date_time")
    val arrDateTime: String?,
    @SerialName("arr_station_code")
    val arrStationCode: String?,
    @SerialName("arr_station_name")
    val arrStationName: String?,
    @SerialName("created_at")
    val createdAt: String?,
    @SerialName("dep_date_time")
    val depDateTime: String?,
    @SerialName("dep_station_code")
    val depStationCode: String?,
    @SerialName("dep_station_name")
    val depStationName: String?,
    @SerialName("display_number")
    val displayNumber: String?,
    @SerialName("in_way_minutes")
    val inWayMinutes: Int?,
    @SerialName("is_talgo")
    val isTalgo: Boolean?,
    val number: String?,
    @SerialName("updated_at")
    val updatedAt: String?,
    @SerialName("with_el_reg")
    val withElReg: Boolean?
)