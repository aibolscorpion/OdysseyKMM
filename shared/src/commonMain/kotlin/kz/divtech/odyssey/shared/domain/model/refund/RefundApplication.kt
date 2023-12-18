package kz.divtech.odyssey.shared.domain.model.refund

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefundApplication(
    @SerialName("application_id")
    val applicationId: Int,
    @SerialName("segments_id")
    val segmentsId: List<Int>,
    val reason: String
)
