package kz.divtech.odyssey.rotation.domain.model.login.search_employee

data class EmployeeResult(
    val employee: EmployeeShort,
    val exists: Boolean
)