package kz.divtech.odyssey.rotation.domain.model.trips

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Train(
    val arr_date_time: String?,
    val arr_station: String?,
    val arr_station_code: String?,
    val arr_station_name: String?,
    val created_at: String?,
    val dep_date_time: String?,
    val dep_station: String?,
    val dep_station_code: String?,
    val dep_station_name: String?,
    val display_number: String?,
    val id: Int?,
    val in_way_minutes: Int?,
    val is_talgo: Boolean?,
    val large_route:@RawValue Any?,
    val number: String?,
    val segment_id: Int?,
    val updated_at: String?,
    val with_el_reg: Boolean?
) : Parcelable