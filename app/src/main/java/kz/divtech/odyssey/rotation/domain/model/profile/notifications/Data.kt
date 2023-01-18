package kz.divtech.odyssey.rotation.domain.model.profile.notifications

import androidx.room.Embedded

data class Data(
    @Embedded val `data`: NotificationData?,
    @Embedded val notification: PushNotification?
)