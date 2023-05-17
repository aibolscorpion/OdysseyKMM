package kz.divtech.odyssey.rotation.domain.model.profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Country(val code: String, val iata_code: String, val id: Int, val name: String): Parcelable{
    override fun toString(): String {
        return name
    }
}