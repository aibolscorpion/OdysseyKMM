package kz.divtech.odyssey.rotation.domain.model.trips.response.trip.stations

import androidx.room.ColumnInfo

data class EndStation(
    @ColumnInfo("end_station_code") val code: String,
    @ColumnInfo("end_station_name") val name: String
)