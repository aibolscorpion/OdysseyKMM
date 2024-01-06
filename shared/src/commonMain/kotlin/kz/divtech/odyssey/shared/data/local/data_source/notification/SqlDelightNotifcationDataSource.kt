package kz.divtech.odyssey.shared.data.local.data_source.notification

import kz.divtech.odssey.database.OdysseyDatabase
import kz.divtech.odyssey.shared.domain.data_source.NotificationDataSource
import kz.divtech.odyssey.shared.domain.model.profile.notifications.Notification

class SqlDelightNotifcationDataSource(database: OdysseyDatabase): NotificationDataSource {
    private val queries = database.notificationQueries
    override suspend fun getNotificationsPagingSource(): List<Notification> {
        return queries.getNotificationsPagingSource().executeAsList().toNotifcationList()
    }

    override suspend fun getFirstThreeNotification(): List<Notification> {
        return queries.getFirstThreeNotifications().executeAsList().toNotifcationList()
    }

    override suspend fun insertNotification(notifications: List<Notification>) {
        queries.transaction {
            notifications.forEach { notification ->
                queries.insertNotification(
                    notification_id = notification.id,
                    notification_type = notification.type,
                    notifiable_type = notification.notifiableType,
                    created_at = notification.createdAt,
                    updated_at = notification.updatedAt,
                    read_at = notification.readAt,
                    id = if(notification.data.id != null) notification.data.id.toLong() else 0,
                    type = notification.data.type,
                    title = notification.data.title,
                    content = notification.data.content,
                    is_important = if(notification.data.isImportant) 1 else 0,
                    application_id = if(notification.data.applicationId != null) notification.data.applicationId.toLong() else 0,
                    segment_id = notification.data.segmentId?.toLong()
                )
            }
        }
    }

    override suspend fun refreshNotifications(notifications: List<Notification>) {
        queries.transaction {
            queries.deleteNotifications()
            notifications.forEach { notification ->
                queries.insertNotification(
                    notification_id = notification.id,
                    notification_type = notification.type,
                    notifiable_type = notification.notifiableType,
                    created_at = notification.createdAt,
                    updated_at = notification.updatedAt,
                    read_at = notification.readAt,
                    id = if(notification.data.id != null) notification.data.id.toLong() else 0,
                    type = notification.data.type,
                    title = notification.data.title,
                    content = notification.data.content,
                    is_important = if(notification.data.isImportant) 1 else 0,
                    application_id = if(notification.data.applicationId != null) notification.data.applicationId.toLong() else 0,
                    segment_id = notification.data.segmentId?.toLong()
                )
            }
        }
    }

    override suspend fun deleteNoficiations() {
        queries.deleteNotifications()
    }
}