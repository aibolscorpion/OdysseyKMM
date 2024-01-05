package kz.divtech.odyssey.shared.domain.model.auth.login.employee_response

import kotlinx.serialization.Serializable
import kz.divtech.odyssey.shared.domain.model.profile.Profile

@Serializable
data class LoginResponse(
    val profile: Profile,
    val organization: Organization,
    val token: String
)