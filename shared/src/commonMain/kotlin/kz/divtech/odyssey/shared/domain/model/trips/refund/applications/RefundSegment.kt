package kz.divtech.odyssey.shared.domain.model.trips.refund.applications

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
data class RefundSegment(
    val id: Int,
    @SerialName("segment_id")
    val segmentId: Int,
    val status: String?,
    @SerialName("error_message")
    val errorMessage: String?,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
): Parcelable