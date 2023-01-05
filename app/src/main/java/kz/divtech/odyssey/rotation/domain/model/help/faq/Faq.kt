package kz.divtech.odyssey.rotation.domain.model.help.faq

import com.google.gson.annotations.SerializedName

data class Faq(val id: Int?,
               val question: String?,
               val answer: String?,
               @SerializedName("created_at") val createdAt: String?,
               @SerializedName("updated_at") val updatedAt: String?)