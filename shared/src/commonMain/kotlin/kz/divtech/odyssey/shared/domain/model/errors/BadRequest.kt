package kz.divtech.odyssey.shared.domain.model.errors

import kotlinx.serialization.Serializable

@Serializable
data class BadRequest(val type: String, val message: String)
