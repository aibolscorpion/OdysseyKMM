package kz.divtech.odyssey.shared.domain.model.employee

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Document(
    val id: Int?,
    @SerialName("expire_date")
    var expireDate: String?,
    @SerialName("issue_by")
    var issueBy: String?,
    @SerialName("issue_date")
    var issueDate: String?,
    var number: String?,
    val type: String,
    @SerialName("use_as_default")
    var useAsDefault: Boolean)
