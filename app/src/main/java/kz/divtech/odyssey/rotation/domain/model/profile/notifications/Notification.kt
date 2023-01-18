package kz.divtech.odyssey.rotation.domain.model.profile.notifications

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Notification(
    val created_at: String?,
    @Embedded val `data`: Data,
    @ColumnInfo(name = "notification_id")
    @PrimaryKey val id: String,
    val notifiable_id: Int?,
    val notifiable_type: String,
    val read_at: String?,
    @ColumnInfo(name = "notification_type")
    val type: String?,
    val updated_at: String
)