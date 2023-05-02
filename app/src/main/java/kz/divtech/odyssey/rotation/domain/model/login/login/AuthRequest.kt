package kz.divtech.odyssey.rotation.domain.model.login.login

data class AuthRequest(val phone: String, val code: String, val auth_log_id: String)
