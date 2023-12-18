package kz.divtech.odyssey.shared.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class DeviceInfo(
    @SerialName("device_os")
    val deviceOS: String?,
    @SerialName("device_type")
    val deviceType: String?,
    val token: String
)
