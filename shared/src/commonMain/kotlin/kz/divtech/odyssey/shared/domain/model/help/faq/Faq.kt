package kz.divtech.odyssey.shared.domain.model.help.faq

import kotlinx.serialization.Serializable

@Serializable
data class Faq(val id: Int?,
               val question: String?,
               val answer: String?)
