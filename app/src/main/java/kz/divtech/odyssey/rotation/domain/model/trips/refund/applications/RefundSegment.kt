package kz.divtech.odyssey.rotation.domain.model.trips.refund.applications

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RefundSegment(
    val created_at: String,
    val error_message: String?,
    val id: Int,
    val refund_application_id: Int,
    val segment_id: Int,
    val status: String?,
    val updated_at: String
): Parcelable