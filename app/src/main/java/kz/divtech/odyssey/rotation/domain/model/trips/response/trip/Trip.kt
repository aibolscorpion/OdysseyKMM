package kz.divtech.odyssey.rotation.domain.model.trips.response.trip

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kz.divtech.odyssey.rotation.domain.model.trips.ActiveTrip
import kz.divtech.odyssey.rotation.domain.model.trips.ArchiveTrip
import kz.divtech.odyssey.rotation.domain.model.trips.refund.applications.RefundAppItem
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.stations.EndStation
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.stations.StartStation
import java.lang.reflect.Type

@TypeConverters(SegmentConverter::class)
@Parcelize
data class Trip(
    @PrimaryKey val id: Int,
    val is_extra: Boolean,
    @Embedded val start_station: @RawValue StartStation,
    @Embedded val end_station: @RawValue EndStation,
    val direction: String?,
    var shift: String?,
    val date: String,
    val status: String,
    val created_at: String,
    val updated_at: String,
    val issued_at: String?,
    val only_bus_transfer: Boolean,
    val segments: List<Segment>,
    val refund_applications: List<RefundAppItem>

) : Parcelable

fun List<Trip>.toActiveTripList() =
    this.map {
        ActiveTrip(it)
    }

fun List<Trip>.toArchiveTripList() =
    this.map {
        ArchiveTrip(it)
    }

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