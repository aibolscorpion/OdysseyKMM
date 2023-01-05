package kz.divtech.odyssey.rotation.domain.model.trips

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Trip(
    val booking_id: Int?,
    val business_trip_days: Int?,
    val created_at: String?,
    val date: String?,
    val deleted_at: String?,
    val direction: String?,
    val employee_id: Int?,
    val end_hours: Int?,
    val end_station: String?,
    val end_station_code: String?,
    val from_old: Boolean?,
    val group_application_id: Int?,
    val id: Int,
    val is_approved: Int?,
    val is_extra: Boolean?,
    val is_stored: Boolean?,
    val issued_at: String?,
    val old_id: Int?,
    val overtime: Int?,
    val product_key: String?,
    val search_by: @RawValue Any?,
    val segments: ArrayList<Segment>?,
    var shift: String?,
    val start_hours: Int?,
    val start_station: String?,
    val start_station_code: String?,
    val status: String?,
    val updated_at: String?,
    val user_id: Int?
) : Parcelable
