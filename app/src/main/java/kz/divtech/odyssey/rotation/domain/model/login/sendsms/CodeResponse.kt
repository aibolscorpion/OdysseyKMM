package kz.divtech.odyssey.rotation.domain.model.login.sendsms

import com.google.gson.annotations.SerializedName

data class CodeResponse(
    @SerializedName("auth_log_id")
    val authLogId: Int)
