package kz.divtech.odyssey.rotation.domain.model.trips

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kz.divtech.odyssey.rotation.domain.model.trips.refund.applications.RefundAppItem
import java.lang.reflect.Type

@TypeConverters(SegmentConverter::class)
@Entity
@Parcelize
data class Trip(
    val booking_id: Int?,
    val business_trip_days: Int?,
    val created_at: String?,
    val date: String?,
    val deleted_at: String?,
    val direction: String?,
    val employee_id: Int?,
    val end_hours: Int?,
    val end_station: String?,
    val end_station_code: String?,
    val from_old: Boolean?,
    val group_application_id: Int?,
    @PrimaryKey val id: Int,
    val is_approved: Int?,
    val is_extra: Boolean?,
    val is_stored: Boolean?,
    val issued_at: String?,
    val old_id: Int?,
    val overtime: Int?,
    val product_key: String?,
    val search_by: @RawValue String?,
    val segments: List<Segment>?,
    var shift: String?,
    val start_hours: Int?,
    val start_station: String?,
    val start_station_code: String?,
    val status: String?,
    val updated_at: String?,
    val user_id: Int?,
    val refund_applications: List<RefundAppItem>
) : Parcelable

class SegmentConverter {
    @TypeConverter
    fun fromString(value: String?): List<Segment> {
        val listType: Type = object : TypeToken<List<Segment>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromData(data: List<Segment>): String {
        val gson = Gson()
        return gson.toJson(data)
    }

    @TypeConverter
    fun fromStringToRefundApplicationsItem(value: String?): List<RefundAppItem> {
        val listType: Type = object : TypeToken<List<RefundAppItem>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromRefundApplicationsItem(data: List<RefundAppItem>): String {
        val gson = Gson()
        return gson.toJson(data)
    }
}