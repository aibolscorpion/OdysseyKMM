package kz.divtech.odyssey.shared.domain.model.sendsms
import kotlinx.serialization.Serializable

@Serializable
data class CodeRequest(
    val phone: String,
    val is_test: Boolean?
)