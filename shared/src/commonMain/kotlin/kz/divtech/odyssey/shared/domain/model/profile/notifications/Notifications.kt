package kz.divtech.odyssey.shared.domain.model.profile.notifications

import kotlinx.serialization.Serializable


@Serializable
data class Notifications(
    val data: List<Notification>,
    val meta: Meta,
    val links: Links
)
