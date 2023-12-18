package kz.divtech.odyssey.shared.domain.model.employee

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: Int,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("first_name")
    var firstName: String,
    @SerialName("last_name")
    var lastName: String,
    var patronymic: String?,
    @SerialName("first_name_en")
    var firstNameEn: String?,
    @SerialName("last_name_en")
    var lastNameEn: String?,
    @SerialName("birth_date")
    var birthDate: String,
    var gender: String,
    @SerialName("country_code")
    var countryCode: String,
    var iin: String,
    val number: String,
    val position: String,
    var phone: String?,
    @SerialName("additional_phone")
    val additionalPhone: String?,
    var email: String?,
    @SerialName("ua_confirmed")
    val uaConfirmed: Boolean,
    val documents: List<Document>
)
