package kz.divtech.odyssey.shared.data.local.data_source.nearest_trip

import database.Nearest_active_trip
import kotlinx.serialization.json.Json
import kz.divtech.odyssey.shared.domain.model.trips.refund.applications.RefundAppItem
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Segment
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Trip
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.stations.EndStation
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.stations.StartStation

fun Nearest_active_trip.toTrip(): Trip{
    val segments: List<Segment> = Json.decodeFromString(segments)
    val refundApplications: List<RefundAppItem> = Json.decodeFromString(refund_applications)
    return Trip(
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
}