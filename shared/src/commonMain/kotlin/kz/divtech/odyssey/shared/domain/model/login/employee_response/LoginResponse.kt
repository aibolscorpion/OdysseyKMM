package kz.divtech.odyssey.shared.domain.model.login.employee_response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val employee: Employee,
    val organization: Organization,
    val token: String
)