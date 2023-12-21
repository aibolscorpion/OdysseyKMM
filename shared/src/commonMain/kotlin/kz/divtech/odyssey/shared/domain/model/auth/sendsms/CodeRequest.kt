package kz.divtech.odyssey.shared.domain.model.auth.sendsms
import kotlinx.serialization.Serializable

@Serializable
data class CodeRequest(
    val phone: String,
    val is_test: Boolean?
)