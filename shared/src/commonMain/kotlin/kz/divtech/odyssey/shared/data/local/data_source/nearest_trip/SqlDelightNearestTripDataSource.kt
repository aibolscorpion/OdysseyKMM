package kz.divtech.odyssey.shared.data.local.data_source.nearest_trip

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kz.divtech.odssey.database.OdysseyDatabase
import kz.divtech.odyssey.shared.domain.data_source.NearestTripDataSource
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Trip

class SqlDelightNearestTripDataSource(database: OdysseyDatabase): NearestTripDataSource {
    private val queries = database.nearestActiveTripQueries
    override suspend fun getNearestTrip(): Trip? {
        return queries.getNearestActiveTrip().executeAsOneOrNull()?.toTrip()
    }

    override suspend fun refreshNearesTrip(trip: Trip) {
        val segments = Json.encodeToString(trip.segments)
        val refundApplications = Json.encodeToString(trip.refundApplications)
        queries.transaction {
            queries.deleteNearestActiveTrip()
            queries.insertNearestActiveTrip(
                id = trip.id.toLong(),
                is_extra = if(trip.isExtra) 1L else 0L,
                direction = trip.direction,
                shift = trip.shift,
                date = trip.date,
                status = trip.status,
                created_at = trip.createdAt,
                updated_at = trip.updatedAt,
                issued_at = trip.issuedAt,
                only_bus_transfer = if(trip.onlyBusTransfer) 1L else 0L,
                segments = segments,
                refund_applications = refundApplications,
                start_station_code = trip.startStation?.code,
                start_station_name = trip.startStation?.name,
                end_station_code = trip.endStation?.code,
                end_station_name = trip.endStation?.name
            )
        }
    }

    override suspend fun deleteNearestTrip() {
        queries.deleteNearestActiveTrip()
    }
}