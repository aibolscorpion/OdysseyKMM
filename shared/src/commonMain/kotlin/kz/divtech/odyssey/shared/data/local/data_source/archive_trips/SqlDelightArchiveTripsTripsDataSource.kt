package kz.divtech.odyssey.shared.data.local.data_source.archive_trips

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kz.divtech.odssey.database.OdysseyDatabase
import kz.divtech.odyssey.shared.domain.data_source.ArchiveTripsDataSource
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Trip

class SqlDelightArchiveTripsTripsDataSource(database: OdysseyDatabase): ArchiveTripsDataSource {
    private val queries = database.archiveTripQueries
    override suspend fun getArchiveTripsSortedByDate(statusType: List<String>, direction: List<String>): List<Trip> {
        return queries.getArchiveTripsSortedByDate(statusType, direction, direction, direction).executeAsList().toTrip()
    }

    override suspend fun getArchiveTripsSortedByStatus(statusType: List<String>, direction: List<String>): List<Trip> {
        return queries.getArchiveTripsSortedByStatus(statusType, direction, direction, direction).executeAsList().toTrip()
    }

    override suspend fun insertArchiveTrips(trips: List<Trip>) {
        queries.transaction {
            trips.forEach { trip ->
                val segments = Json.encodeToString(trip.segments)
                val refundApplications = Json.encodeToString(trip.refundApplications)
                queries.insertArchiveTrips(
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

    override suspend fun refreshArchiveTrips(trips: List<Trip>) {
        queries.transaction {
            queries.deleteArchiveTrips()
            trips.forEach { trip ->
                val segments = Json.encodeToString(trip.segments)
                val refundApplications = Json.encodeToString(trip.refundApplications)
                queries.insertArchiveTrips(
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

    override suspend fun deleteArchiveTrips() {
        queries.deleteArchiveTrips()
    }
}