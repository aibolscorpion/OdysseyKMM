package kz.divtech.odyssey.shared.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePhoneRequest(
    @SerialName("employee_id")
    val employeeId: Int,
    val phone: String,
    @SerialName("firebase_token")
    val firebaseToken: String
)
