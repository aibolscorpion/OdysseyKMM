package kz.divtech.odyssey.shared.domain.model.profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
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
    var useAsDefault: Boolean
): Parcelable
