package kz.divtech.odyssey.shared.domain.model.login

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(val phone: String, val code: String, val auth_log_id: Int)
