package kz.divtech.odyssey.shared.domain.model.trips.response.trip

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ticket(
    val id: Int,
    @SerialName("express_id")
    val expressId: String,
    @SerialName("dep_station_code")
    val depStationCode: String,
    @SerialName("dep_station_name")
    val depStationName: String,
    @SerialName("arr_station_code")
    val arrStationCode: String?,
    @SerialName("arr_station_name")
    val arrStationName: String?,
    @SerialName("train_number")
    val trainNumber: String,
    @SerialName("train_display_number")
    val trainDisplayNumber: String,
    @SerialName("is_talgo_train")
    val isTalgoTrain: Boolean,
    @SerialName("dep_date_time")
    val depDateTime: String,
    @SerialName("arr_date_time")
    val arrDateTime: String,
    @SerialName("car_type_label")
    val carTypeLabel: String,
    @SerialName("car_number")
    val carNumber: String,
    @SerialName("car_class")
    val carClass: String,
    @SerialName("seat_number")
    val seatNumber: String,
    @SerialName("carrier_name")
    val carrierName: String,
    val sum: Int,
    @SerialName("return_sum")
    val returnSum: Int?,
    @SerialName("return_commission")
    val returnCommission: String?,
    @SerialName("returned_at")
    val returnedAt: String?,
    val status: String,
    @SerialName("issued_at")
    val issuedAt: String?,
    @SerialName("booked_at")
    val bookedAt: String?,
    @SerialName("ticket_url")
    val ticketUrl: String?
)