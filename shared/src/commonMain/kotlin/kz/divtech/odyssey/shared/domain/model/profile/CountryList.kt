package kz.divtech.odyssey.shared.domain.model.profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CountryList(
    val countries: List<Country>
): Parcelable