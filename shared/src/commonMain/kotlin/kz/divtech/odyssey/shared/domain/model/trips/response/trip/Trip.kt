package kz.divtech.odyssey.shared.domain.model.trips.response.trip

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kz.divtech.odyssey.shared.domain.model.trips.refund.applications.RefundAppItem
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.stations.EndStation
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.stations.StartStation

@Serializable
@Parcelize
data class Trip(
    val id: Int,
    @SerialName("is_extra")
    val isExtra: Boolean,
    @SerialName("start_station")
    val startStation: StartStation?,
    @SerialName("end_station")
    val endStation: EndStation?,
    val direction: String?,
    var shift: String?,
    val date: String,
    val status: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("issued_at")
    val issuedAt: String?,
    @SerialName("only_bus_transfer")
    val onlyBusTransfer: Boolean,
    val segments: List<Segment>,
    @SerialName("refund_applications")
    val refundApplications: List<RefundAppItem>
): Parcelable