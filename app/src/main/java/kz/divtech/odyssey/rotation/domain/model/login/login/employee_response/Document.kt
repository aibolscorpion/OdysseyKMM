package kz.divtech.odyssey.rotation.domain.model.login.login.employee_response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Document(
    var expire_date: String?,
    val id: Int?,
    var issue_by: String?,
    var issue_date: String?,
    var number: String?,
    val type: String,
    var use_as_default: Boolean
): Parcelable