package kz.divtech.odyssey.rotation.domain.model.trips.response.trip.stations

import androidx.room.ColumnInfo

data class StartStation(
    @ColumnInfo("start_station_code") val code: String,
    @ColumnInfo("start_station_name") val name: String
)