package kz.divtech.odyssey.rotation.domain.model.login.search_employee

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EmployeeShort(
    val full_name: String,
    val id: Int,
    val position: String,
    val phone: String?,
    val status: String
): Parcelable