package kz.divtech.odyssey.shared.domain.model.auth.search_employee

import kotlinx.serialization.Serializable

@Serializable
data class EmployeeShort(
    val full_name: String,
    val id: Int,
    val position: String,
    val phone: String?,
    val status: String
)