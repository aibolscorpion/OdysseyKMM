package kz.divtech.odyssey.rotation.domain.model.profile.notifications

import kz.divtech.odyssey.rotation.domain.model.trips.response.Links
import kz.divtech.odyssey.rotation.domain.model.trips.response.Meta

data class Notifications(
    val data: List<Notification>,
    val meta: Meta,
    val links: Links
)