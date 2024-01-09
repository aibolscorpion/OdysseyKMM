package kz.divtech.odyssey.shared.domain.model.auth.search_employee

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class EmployeeShort(
    @SerialName("full_name")
    val fullName: String,
    val id: Int,
    val position: String,
    val phone: String?,
    val status: String
): Parcelable