package kz.divtech.odyssey.shared.domain.model.sendsms

import kotlinx.serialization.Serializable

@Serializable
data class CodeResponse(val auth_log_id: Int)
