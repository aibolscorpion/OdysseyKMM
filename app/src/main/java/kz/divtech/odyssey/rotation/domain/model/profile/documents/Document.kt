package kz.divtech.odyssey.rotation.domain.model.profile.documents

data class Document(
    val created_at: String?,
    val employee_id: Int?,
    val expire_date: Any?,
    val id: Int?,
    val issue_by: String?,
    val issue_date: String?,
    val number: String?,
    val type: String?,
    val updated_at: String?
)