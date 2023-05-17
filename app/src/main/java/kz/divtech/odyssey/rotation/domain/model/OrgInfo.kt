package kz.divtech.odyssey.rotation.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class OrgInfo(
    @PrimaryKey
    @SerializedName("support_phone")
    val supportPhone: String,
    @SerializedName("telegram_id")
    val telegramId: String?,
    @SerializedName("whatsapp_phone")
    val whatsappPhone: String
)