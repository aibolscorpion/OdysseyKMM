package kz.divtech.odyssey.rotation.domain.model.trips.response.trip

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Train(
    val arr_date_time: String?,
    val arr_station_code: String?,
    val arr_station_name: String?,
    val created_at: String?,
    val dep_date_time: String?,
    val dep_station_code: String?,
    val dep_station_name: String?,
    val display_number: String?,
    val in_way_minutes: Int?,
    val is_talgo: Boolean?,
    val number: String?,
    val updated_at: String?,
    val with_el_reg: Boolean?
) : Parcelable