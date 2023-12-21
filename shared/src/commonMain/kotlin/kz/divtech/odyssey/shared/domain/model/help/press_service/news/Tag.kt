package kz.divtech.odyssey.shared.domain.model.help.press_service.news

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val id: Int?,
    val title: String?,
)