package kz.divtech.odyssey.shared.data.local.data_source.active_trips

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kz.divtech.odssey.database.OdysseyDatabase
import kz.divtech.odyssey.shared.domain.data_source.ActiveTripDataSource
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Trip

class SqlDelightActiveTripsDataSource(database: OdysseyDatabase): ActiveTripDataSource {
    private val queries = database.activeTripQueries
    override suspend fun getActiveTripsSortedByDate(statusType: List<String>, direction: List<String>): List<Trip> {
        return queries.getActiveTripsSortedByDate(statusType, direction, direction, direction).executeAsList().toTrip()
    }

    override suspend fun getActiveTripsSortedByStatus(statusType: List<String>, direction: List<String>): List<Trip> {
        return queries.getActiveTripsSortedByStatus(statusType, direction, direction, direction).executeAsList().toTrip()
    }

    override suspend fun insertActiveTrips(trips: List<Trip>) {
        queries.transaction {
            trips.forEach { trip ->
                val segments = Json.encodeToString(trip.segments)
                val refundApplications = Json.encodeToString(trip.refundApplications)
                queries.insertActiveTrips(
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
    }

    override suspend fun refreshActiveTrips(trips: List<Trip>) {
        queries.transaction {
            queries.deleteActiveTrips()
            trips.forEach { trip ->
                val segments = Json.encodeToString(trip.segments)
                val refundApplications = Json.encodeToString(trip.refundApplications)
                queries.insertActiveTrips(
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
    }

    override suspend fun deleteActiveTrips() {
        queries.deleteActiveTrips()
    }
}