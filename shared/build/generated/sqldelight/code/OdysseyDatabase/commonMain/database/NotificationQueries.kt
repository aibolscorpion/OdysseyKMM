package database

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Long
import kotlin.String

public class NotificationQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> getNotifications(mapper: (
    notification_id: String,
    notification_type: String,
    notifiable_type: String,
    created_at: String,
    updated_at: String,
    read_at: String?,
    id: Long,
    title: String,
    content: String,
    is_important: Long,
    type: String,
    application_id: Long,
    segment_id: Long?,
  ) -> T): Query<T> = Query(-778_131_574, arrayOf("Notification"), driver, "Notification.sq",
      "getNotifications", "SELECT * FROM Notification ORDER BY created_at DESC") { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5),
      cursor.getLong(6)!!,
      cursor.getString(7)!!,
      cursor.getString(8)!!,
      cursor.getLong(9)!!,
      cursor.getString(10)!!,
      cursor.getLong(11)!!,
      cursor.getLong(12)
    )
  }

  public fun getNotifications(): Query<Notification> = getNotifications { notification_id,
      notification_type, notifiable_type, created_at, updated_at, read_at, id, title, content,
      is_important, type, application_id, segment_id ->
    Notification(
      notification_id,
      notification_type,
      notifiable_type,
      created_at,
      updated_at,
      read_at,
      id,
      title,
      content,
      is_important,
      type,
      application_id,
      segment_id
    )
  }

  public fun <T : Any> getFirstThreeNotifications(mapper: (
    notification_id: String,
    notification_type: String,
    notifiable_type: String,
    created_at: String,
    updated_at: String,
    read_at: String?,
    id: Long,
    title: String,
    content: String,
    is_important: Long,
    type: String,
    application_id: Long,
    segment_id: Long?,
  ) -> T): Query<T> = Query(-1_810_849_060, arrayOf("Notification"), driver, "Notification.sq",
      "getFirstThreeNotifications", "SELECT * FROM Notification ORDER BY created_at DESC LIMIT 3") {
      cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5),
      cursor.getLong(6)!!,
      cursor.getString(7)!!,
      cursor.getString(8)!!,
      cursor.getLong(9)!!,
      cursor.getString(10)!!,
      cursor.getLong(11)!!,
      cursor.getLong(12)
    )
  }

  public fun getFirstThreeNotifications(): Query<Notification> = getFirstThreeNotifications {
      notification_id, notification_type, notifiable_type, created_at, updated_at, read_at, id,
      title, content, is_important, type, application_id, segment_id ->
    Notification(
      notification_id,
      notification_type,
      notifiable_type,
      created_at,
      updated_at,
      read_at,
      id,
      title,
      content,
      is_important,
      type,
      application_id,
      segment_id
    )
  }

  public fun insertNotifications(
    notification_id: String,
    notification_type: String,
    notifiable_type: String,
    created_at: String,
    updated_at: String,
    read_at: String?,
    id: Long,
    title: String,
    content: String,
    is_important: Long,
    type: String,
    application_id: Long,
    segment_id: Long?,
  ) {
    driver.execute(-1_980_833_449, """
        |INSERT OR REPLACE INTO Notification(notification_id, notification_type, notifiable_type, created_at,
        |updated_at, read_at, id, title, content, is_important, type, application_id, segment_id)
        |VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """.trimMargin(), 13) {
          bindString(0, notification_id)
          bindString(1, notification_type)
          bindString(2, notifiable_type)
          bindString(3, created_at)
          bindString(4, updated_at)
          bindString(5, read_at)
          bindLong(6, id)
          bindString(7, title)
          bindString(8, content)
          bindLong(9, is_important)
          bindString(10, type)
          bindLong(11, application_id)
          bindLong(12, segment_id)
        }
    notifyQueries(-1_980_833_449) { emit ->
      emit("Notification")
    }
  }

  public fun deleteNotifications() {
    driver.execute(-544_409_691, """DELETE FROM Notification""", 0)
    notifyQueries(-544_409_691) { emit ->
      emit("Notification")
    }
  }
}
