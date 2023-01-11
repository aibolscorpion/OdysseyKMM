package kz.divtech.odyssey.rotation.domain.model.help.faq

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Faq(@PrimaryKey val id: Int?,
               val question: String?,
               val answer: String?,
               @SerializedName("created_at") val createdAt: String?,
               @SerializedName("updated_at") val updatedAt: String?)