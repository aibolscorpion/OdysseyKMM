package kz.divtech.odyssey.rotation.domain.model.login.login

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Employee(val id: Int?, @SerializedName("first_name") val firstName: String,
                    @SerializedName("last_name") val lastName: String?, val patronymic: String?,
                    @SerializedName("first_name_en") val firstNameEn: String?,
                    @SerializedName("last_name_en") val lastNameEn: String?, val iin: String?, val position: String?,
                    @SerializedName("is_phone_number") val isPhoneNumber: Boolean?, val status: String?,
                    @SerializedName("country_code") val countryCode: String?, @SerializedName("org_name") val orgName: String?,
                    val number: String?, val gender: String?, @SerializedName("birth_date") val birthDate: String?) : Parcelable