package kz.divtech.odyssey.shared.data.local.data_source.notification

import kz.divtech.odyssey.shared.domain.model.profile.notifications.Notification
import kz.divtech.odyssey.shared.domain.model.profile.notifications.NotificationData

fun List<database.Notification>.toNotifcationList(): List<Notification>{
    return this.map {
        Notification(
            id = it.notification_id,
            data = NotificationData(
                id = it.id.toInt(),
                title = it.title,
                content = it.title,
                isImportant = it.is_important == 1L,
                type = it.notification_type,
                applicationId = it.application_id.toInt(),
                segmentId = it.segment_id?.toInt()
                ),
            type = it.notifiable_type,
            notifiableType = it.notifiable_type,
            createdAt = it.created_at,
            updatedAt = it.updated_at,
            readAt = it.read_at
        )
    }
}