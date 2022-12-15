package kz.divtech.odyssey.rotation.domain.model.profile.notifications

data class Data(
    val created_at: String,
    val `data`: DataX,
    val id: String,
    val notifiable_id: Int,
    val notifiable_type: String,
    val read_at: Any,
    val type: String,
    val updated_at: String
)