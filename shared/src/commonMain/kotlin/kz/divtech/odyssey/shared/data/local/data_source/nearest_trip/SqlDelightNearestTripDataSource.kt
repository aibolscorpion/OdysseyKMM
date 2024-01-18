package kz.divtech.odyssey.shared.data.local.data_source.nearest_trip

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kz.divtech.odssey.database.OdysseyDatabase
import kz.divtech.odyssey.shared.domain.data_source.NearestTripDataSource
import kz.divtech.odyssey.shared.domain.model.trips.refund.applications.RefundAppItem
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Segment
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Trip
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.stations.EndStation
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.stations.StartStation

class SqlDelightNearestTripDataSource(database: OdysseyDatabase): NearestTripDataSource {
    private val queries = database.nearestActiveTripQueries
    override fun getNearestTrip(): Flow<Trip?> {
        return queries.getNearestActiveTrip(mapper = { id, is_extra, direction, shift, date,
            status, created_at, updated_at, issued_at, only_bus_transfer, segments, refund_applications,
            start_station_code, start_station_name, end_station_code, end_station_name ->
            val segments: List<Segment> = Json.decodeFromString(segments)
            val refundApplications: List<RefundAppItem> = Json.decodeFromString(refund_applications)
            Trip(
                id = id.toInt(),
                isExtra = is_extra == 1L,
                startStation = StartStation(code = start_station_code, name = start_station_name),
                endStation = EndStation(code = end_station_code, name = end_station_name),
                direction = direction,
                shift = shift,
                date = date,
                status = status,
                createdAt = created_at,
                updatedAt = updated_at,
                issuedAt = issued_at,
                onlyBusTransfer = only_bus_transfer == 1L,
                segments = segments,
                refundApplications = refundApplications
            )
        }).asFlow().mapToOneOrNull(Dispatchers.IO)
    }

    override fun refreshNearesTrip(trip: Trip) {
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

    override fun deleteNearestTrip() {
        queries.deleteNearestActiveTrip()
    }
}