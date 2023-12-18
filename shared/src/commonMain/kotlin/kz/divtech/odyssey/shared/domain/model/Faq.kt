package kz.divtech.odyssey.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Faq(val id: Int?,
               val question: String?,
               val answer: String?)
