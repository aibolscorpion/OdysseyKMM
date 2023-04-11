package kz.divtech.odyssey.rotation.domain.model.trips.refund.applications

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RefundAppItem(
    val application_id: Int,
    val created_at: String,
    val employee_id: Int,
    val id: Int,
    val reason: String,
    val reject_reason: String?,
    val segments: List<RefundSegment>,
    val status: String,
    val updated_at: String,
    val user_id: Int?
): Parcelable