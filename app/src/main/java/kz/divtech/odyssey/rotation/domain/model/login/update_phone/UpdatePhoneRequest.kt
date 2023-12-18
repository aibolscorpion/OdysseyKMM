package kz.divtech.odyssey.rotation.domain.model.login.update_phone

import com.google.gson.annotations.SerializedName

data class UpdatePhoneRequest(@SerializedName("employee_id")
                              val employeeId: Int,
                              val phone: String,
                              @SerializedName("firebase_token")
                              val firebaseToken: String)