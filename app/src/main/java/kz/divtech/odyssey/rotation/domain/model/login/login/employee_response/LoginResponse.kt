package kz.divtech.odyssey.rotation.domain.model.login.login.employee_response

data class LoginResponse(
    val employee: Employee,
    val organization: Organization,
    val token: String
)