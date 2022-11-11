package kz.divtech.odyssey.rotation.domain.model.login.update_phone

import com.google.gson.annotations.SerializedName

data class UpdatePhoneRequest(@SerializedName("employee_id") val id: Int?, @SerializedName("employee_number") val number: String?,
                              @SerializedName("first_name") val firstName: String?, @SerializedName("last_name") val lastName: String?,
                              val iin: String?, @SerializedName("phone_number") val phoneNumber: String?,
                              @SerializedName("firebase_token") val firebaseToken: String?)