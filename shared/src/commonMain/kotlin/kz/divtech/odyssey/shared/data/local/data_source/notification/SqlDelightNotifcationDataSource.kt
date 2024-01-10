package kz.divtech.odyssey.shared.data.local.data_source.notification

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kz.divtech.odssey.database.OdysseyDatabase
import kz.divtech.odyssey.shared.domain.data_source.NotificationDataSource
import kz.divtech.odyssey.shared.domain.model.profile.notifications.Notification
import kz.divtech.odyssey.shared.domain.model.profile.notifications.NotificationData

class SqlDelightNotifcationDataSource(database: OdysseyDatabase): NotificationDataSource {
    private val queries = database.notificationQueries
    override suspend fun getNotificationsPagingSource(): List<Notification> {
        return queries.getNotificationsPagingSource().executeAsList().toNotifcationList()
    }

    override suspend fun getFirstThreeNotification(): Flow<List<Notification>> {
        return queries.getFirstThreeNotifications(mapper = {
             notification_id, notification_type, notifiable_type,
             created_at, updated_at, read_at, id, title,
             content, is_important,  type, application_id, segment_id ->
            Notification(
                id = notification_id,
                data = NotificationData(
                    id = id.toInt(),
                    title = title,
                    content = content,
                    isImportant = is_important == 1L,
                    type = type,
                    applicationId = application_id.toInt(),
                    segmentId = segment_id?.toInt()
                ),
                type = notification_type,
                notifiableType = notifiable_type,
                createdAt = created_at,
                updatedAt = updated_at,
                readAt = read_at)
        }).asFlow().mapToList(Dispatchers.IO)
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