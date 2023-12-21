package kz.divtech.odyssey.shared.domain.model.profile.notifications

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarkNotification(
    @SerialName("notification_id")
    val notificationId: String
)
