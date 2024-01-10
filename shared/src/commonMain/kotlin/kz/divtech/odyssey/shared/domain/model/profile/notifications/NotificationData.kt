package kz.divtech.odyssey.shared.domain.model.profile.notifications

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class NotificationData(
    val id: Int?,
    val title: String,
    val content: String,
    @SerialName("is_important")
    val isImportant: Boolean,
    val type: String,
    @SerialName("application_id")
    val applicationId: Int?,
    @SerialName("segment_id")
    val segmentId: Int?
): Parcelable