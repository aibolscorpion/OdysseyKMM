package kz.divtech.odyssey.rotation.domain.model.profile.notifications

import androidx.room.ColumnInfo

data class PushNotificationData(
    @ColumnInfo(name = "push_notification_body")
    val body: String,
    @ColumnInfo(name = "push_notification_title")
    val title: String
)