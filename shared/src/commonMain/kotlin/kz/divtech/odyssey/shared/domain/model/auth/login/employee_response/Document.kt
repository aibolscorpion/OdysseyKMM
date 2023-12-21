package kz.divtech.odyssey.shared.domain.model.auth.login.employee_response

import kotlinx.serialization.Serializable


@Serializable
data class Document(
    var expire_date: String?,
    val id: Int?,
    var issue_by: String?,
    var issue_date: String?,
    var number: String?,
    val type: String,
    var use_as_default: Boolean
)
