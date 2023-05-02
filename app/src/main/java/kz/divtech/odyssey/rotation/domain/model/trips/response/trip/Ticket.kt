package kz.divtech.odyssey.rotation.domain.model.trips.response.trip

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ticket(
    val id: Int,
    val express_id: String,
    val dep_station_code: String,
    val dep_station_name: String,
    val arr_station_code: String?,
    val arr_station_name: String?,
    val train_number: String,
    val train_display_number: String,
    val is_talgo_train: Boolean,
    val dep_date_time: String,
    val arr_date_time: String,
    val car_type_label: String,
    val car_number: String,
    val car_class: String,
    val seat_number: String,
    val carrier_name: String,
    val sum: Int,
    val return_sum: Int?,
    val return_commission: String?,
    val returned_at: String?,
    val status: String,
    val issued_at: String?,
    val booked_at: String?,
    val ticket_url: String?
) : Parcelable