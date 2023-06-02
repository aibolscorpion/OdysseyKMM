package kz.divtech.odyssey.rotation.domain.model.profile.notifications

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Entity
@Parcelize
data class Notification(
    @ColumnInfo(name = "notification_id")
    @PrimaryKey val id: String,
    @Embedded val data: @RawValue NotificationData,
    @ColumnInfo(name = "notification_type")
    val type: String,
    val notifiable_type: String,
    val created_at: String,
    val updated_at: String,
    val read_at: String?) : Parcelable{

    fun convertToPushNotification(): PushNotification{
        return PushNotification(id, type, created_at, data.title,
            data.content, data.type, data.is_important, data.application_id)
    }
}
