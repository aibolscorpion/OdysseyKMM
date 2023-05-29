package kz.divtech.odyssey.rotation.domain.model.trips.response.trip

import androidx.room.Embedded
import androidx.room.Entity

@Entity(tableName = "nearest_active_trip", primaryKeys = ["id"])
data class SingleTrip(@Embedded val data: Trip?)
