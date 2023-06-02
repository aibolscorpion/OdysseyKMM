package kz.divtech.odyssey.rotation.domain.model.profile.notifications

data class NotificationData(
    val id: Int,
    val title: String,
    val content: String,
    val is_important: Boolean,
    val type: String,
    val application_id: Int,
    val segment_id: Int?
)