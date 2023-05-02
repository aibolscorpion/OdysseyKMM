package kz.divtech.odyssey.rotation.domain.model.trips.refund.applications

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RefundSegment(
    val id: Int,
    val segment_id: Int,
    val status: String?,
    val error_message: String?,
    val created_at: String,
    val updated_at: String
): Parcelable