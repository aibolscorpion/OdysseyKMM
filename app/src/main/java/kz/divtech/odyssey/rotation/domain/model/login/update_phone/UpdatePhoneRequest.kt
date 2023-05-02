package kz.divtech.odyssey.rotation.domain.model.login.update_phone

import com.google.gson.annotations.SerializedName

data class UpdatePhoneRequest(@SerializedName("employee_id") val id: Int,
                              @SerializedName("phone") val phoneNumber: String,
                              @SerializedName("firebase_token") val firebaseToken: String)