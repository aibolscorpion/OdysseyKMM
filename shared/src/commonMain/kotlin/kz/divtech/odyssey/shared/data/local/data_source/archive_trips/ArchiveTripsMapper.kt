package kz.divtech.odyssey.shared.data.local.data_source.archive_trips

import database.Archive_trip
import kotlinx.serialization.json.Json
import kz.divtech.odyssey.shared.domain.model.trips.refund.applications.RefundAppItem
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Segment
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Trip
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.stations.EndStation
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.stations.StartStation

fun List<Archive_trip>.toTrip(): List<Trip>{
    return map {
        val segments: List<Segment> = Json.decodeFromString(it.segments)
        val refundApplications: List<RefundAppItem> = Json.decodeFromString(it.refund_applications)
        Trip(
            id = it.id.toInt(),
            isExtra = it.is_extra == 1L,
            startStation = StartStation(code = it.start_station_code, name = it.start_station_name),
            endStation = EndStation(code = it.end_station_code, name = it.end_station_name),
            direction = it.direction,
            shift = it.shift,
            date = it.date,
            status = it.status,
            createdAt = it.created_at,
            updatedAt = it.updated_at,
            issuedAt = it.issued_at,
            onlyBusTransfer = it.only_bus_transfer == 1L,
            segments = segments,
            refundApplications = refundApplications
        )
    }
}
