package kz.divtech.odyssey.shared.domain.model.article

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val id: Int?,
    val title: String?)
