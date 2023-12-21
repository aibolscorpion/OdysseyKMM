package kz.divtech.odyssey.shared.domain.model.profile.notifications

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val id: String,
    val data: NotificationData,
    val type: String,
    @SerialName("notifiable_type")
    val notifiableType: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("read_at")
    val readAt: String?)
