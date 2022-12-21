package kz.divtech.odyssey.rotation.domain.model.login.login

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Employee(@PrimaryKey val id: Int?,
                    @SerializedName("first_name") var firstName: String?,
                    @SerializedName("last_name") var lastName: String?, var patronymic: String?,
                    @SerializedName("first_name_en") var firstNameEn: String?,
                    @SerializedName("last_name_en") var lastNameEn: String?, var iin: String?, val position: String?,
                    @SerializedName("is_phone_number") val isPhoneNumber: Boolean?, val status: String?,
                    @SerializedName("country_code") val countryCode: String?,
                    @SerializedName("country_name") var countryName: String?,
                    @SerializedName("org_name") val orgName: String?, val number: String?,
                    var gender: String?, @SerializedName("birth_date") var birthDate: String?,
                    var phone: String?, var email: String?) : Parcelable