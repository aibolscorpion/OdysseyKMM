package kz.divtech.odyssey.rotation.domain.model.trips

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Place(
    val category_name: String?,
    val category_place_param: @RawValue Any?,
    val category_place_param_ind: @RawValue Any?,
    val comp_number: Int?,
    val created_at: String?,
    val floor: Int?,
    val id: Int?,
    val is_border: Int?,
    val level: @RawValue Any?,
    val number: Int?,
    val ticket_id: Int?,
    val type: String?,
    val updated_at: String?
) : Parcelable