package kz.divtech.odyssey.shared.domain.model.trips.refund.applications

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Segment

@Serializable
@Parcelize
data class RefundAppItem(
    val id: Int,
    val reason: String,
    val status: String,
    @SerialName("reject_reason")
    val rejectReason: String?,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    val segments: List<RefundSegment>,
    var realSegment: List<Segment>?
): Parcelable