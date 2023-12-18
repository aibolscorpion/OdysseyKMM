package kz.divtech.odyssey.shared.domain.model.login.employee_response

import kotlinx.serialization.Serializable

@Serializable
data class Employee(
    val id: Int,
    val full_name: String,
    var first_name: String,
    var last_name: String,
    var patronymic: String?,
    var first_name_en: String?,
    var last_name_en: String?,
    var birth_date: String,
    var gender: String,
    var country_code: String,
    var iin: String,
    val number: String,
    val position: String,
    var phone: String?,
    val additional_phone: String?,
    var email: String?,
    val ua_confirmed: Boolean,
    val documents: List<Document>
)
