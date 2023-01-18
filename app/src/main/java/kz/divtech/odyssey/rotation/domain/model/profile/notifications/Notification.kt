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
    val created_at: String?,
    @Embedded
    val `data`: @RawValue Data,
    @ColumnInfo(name = "notification_id")
    @PrimaryKey val id: String,
    val notifiable_id: Int?,
    val notifiable_type: String,
    val read_at: String?,
    @ColumnInfo(name = "notification_type")
    val type: String?,
    val updated_at: String
) : Parcelable