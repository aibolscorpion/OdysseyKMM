package kz.divtech.odyssey.shared.domain.model.errors

import kotlinx.serialization.Serializable

@Serializable
data class ValidationErrorResponse(
    val errors: Map<String, List<String>>,
    val type: String
)
