package kz.divtech.odyssey.shared.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class OrgInfo(
    @SerialName("support_phone")
    val supportPhone: String,
    @SerialName("telegram_id")
    val telegramId: String?,
    @SerialName("whatsapp_phone")
    val whatsappPhone: String
)