package kz.divtech.odyssey.rotation.domain.model.login.sendsms

data class CodeRequest(
    val phone: String,
    val is_test: Boolean?
)