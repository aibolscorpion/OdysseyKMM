package kz.divtech.odyssey.rotation.domain.model.errors

data class ValidationErrorResponse(
    val errors: Map<String, List<String>>,
    val type: String
)