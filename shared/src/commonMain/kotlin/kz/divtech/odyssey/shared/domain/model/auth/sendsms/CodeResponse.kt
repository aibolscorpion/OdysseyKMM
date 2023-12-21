package kz.divtech.odyssey.shared.domain.model.auth.sendsms

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CodeResponse(
    @SerialName("auth_log_id")
    val authLogId: Int)
