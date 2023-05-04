package kz.divtech.odyssey.rotation.domain.model.help.press_service.news

import kz.divtech.odyssey.rotation.domain.model.trips.response.Links
import kz.divtech.odyssey.rotation.domain.model.trips.response.Meta

data class News(
    val data: List<Article>,
    val links: Links,
    val meta: Meta
)

