package kz.divtech.odyssey.shared.domain.model.search_employee

import kotlinx.serialization.Serializable

@Serializable
data class EmployeeResult(
    val employee: EmployeeShort?,
    val exists: Boolean,
    val url: String?
)