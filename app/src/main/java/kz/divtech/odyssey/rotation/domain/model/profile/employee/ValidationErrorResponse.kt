package kz.divtech.odyssey.rotation.domain.model.profile.employee

data class ValidationErrorResponse(
    val errors: Map<String, List<String>>,
    val type: String
)