package kz.divtech.odyssey.rotation.domain.model.fdf

data class ValidationErrorResponse(
    val errors: Map<String, List<String>>,
    val type: String
)