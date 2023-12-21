package kz.divtech.odyssey.shared.domain.model.auth.login.employee_response

import kotlinx.serialization.Serializable

@Serializable
data class Organization(
    val name: String
)