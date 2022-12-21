package kz.divtech.odyssey.rotation.domain.model.profile.documents

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Document(
    val created_at: String?,
    val employee_id: Int?,
    var expire_date: String?,
    val id: Int?,
    var issue_by: String?,
    var issue_date: String?,
    var number: String?,
    val type: String?,
    val updated_at: String?
): Parcelable