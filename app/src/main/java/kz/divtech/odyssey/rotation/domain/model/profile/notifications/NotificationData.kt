package kz.divtech.odyssey.rotation.domain.model.profile.notifications

data class NotificationData(
    val application_id: Int,
    val by_watcher: Boolean?,
    val content: String,
    val content_available: Boolean?,
    val id: Int,
    val is_important: Boolean,
    val priority: String?,
    val segment_id: Int?,
    val title: String,
    val type: String
)