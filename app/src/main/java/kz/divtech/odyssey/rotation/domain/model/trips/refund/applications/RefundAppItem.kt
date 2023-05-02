package kz.divtech.odyssey.rotation.domain.model.trips.refund.applications

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Segment

@Parcelize
data class RefundAppItem(
    val id: Int,
    val reason: String,
    val status: String,
    val reject_reason: String?,
    val created_at: String,
    val updated_at: String,
    val segments: List<RefundSegment>,
    var realSegment: List<Segment>?
): Parcelable