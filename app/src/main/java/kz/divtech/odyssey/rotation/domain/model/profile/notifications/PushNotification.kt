package kz.divtech.odyssey.rotation.domain.model.profile.notifications

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PushNotification(
    val id: String,
    val notifiableType: String,
    val sendTime: String,
    val title: String,
    val content: String,
    val type: String,
    val isImportant: Boolean,
    val applicationId: Int?): Parcelable