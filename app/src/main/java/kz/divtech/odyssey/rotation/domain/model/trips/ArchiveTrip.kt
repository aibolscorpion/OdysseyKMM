package kz.divtech.odyssey.rotation.domain.model.trips

import androidx.room.Embedded
import androidx.room.Entity
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Trip

@Entity(tableName = "archive_trip", primaryKeys = ["id"])
data class ArchiveTrip(@Embedded val trip: Trip)