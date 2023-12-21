package kz.divtech.odyssey.shared.domain.model.profile.notifications

import kotlinx.serialization.Serializable

@Serializable
data class Links(
    val first: String,
    val last: String,
    val next: String?,
    val prev: String?
)