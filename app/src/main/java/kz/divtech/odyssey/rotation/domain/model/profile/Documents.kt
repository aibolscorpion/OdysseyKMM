package kz.divtech.odyssey.rotation.domain.model.profile

import androidx.room.Entity

@Entity
data class Documents(
    val documents: List<Document>?
)