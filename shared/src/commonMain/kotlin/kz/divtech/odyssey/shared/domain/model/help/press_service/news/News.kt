package kz.divtech.odyssey.shared.domain.model.help.press_service.news

import kotlinx.serialization.Serializable
import kz.divtech.odyssey.shared.domain.model.profile.notifications.Links
import kz.divtech.odyssey.shared.domain.model.profile.notifications.Meta

@Serializable
data class News(
    val data: List<Article>,
    val links: Links,
    val meta: Meta
)