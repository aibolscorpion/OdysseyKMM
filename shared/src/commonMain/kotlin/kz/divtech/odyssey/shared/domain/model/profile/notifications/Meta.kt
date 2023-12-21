package kz.divtech.odyssey.shared.domain.model.profile.notifications

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Meta(
    @SerialName("current_page")
    val currentPage: Int,
    val from: Int,
    @SerialName("last_page")
    val lastPage: Int,
    val path: String,
    @SerialName("per_page")
    val perPage: Int,
    val to: Int,
    val total: Int
)