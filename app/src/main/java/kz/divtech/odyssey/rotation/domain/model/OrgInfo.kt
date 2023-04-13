package kz.divtech.odyssey.rotation.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class OrgInfo(
    val address: String?,
    val fullName: String?,
    val issue_phone: String?,
    val latitude: String?,
    val logotype_url: String,
    val longitude: String?,
    @PrimaryKey
    val shortName: String,
    @SerializedName("support_phone")
    val supportPhone: String,
    @SerializedName("telegram_phone")
    val telegramPhone: String?,
    @SerializedName("whatsapp_phone")
    val whatsappPhone: String
)