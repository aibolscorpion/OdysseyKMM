package kz.divtech.odyssey.rotation.domain.model.trips


data class Trips(
    val current_page: Int?,
    val data: List<Trip>?,
    val first_page_url: String?,
    val from: Int?,
    val last_page: Int?,
    val last_page_url: String?,
    val next_page_url: Any?,
    val path: String?,
    val per_page: Int?,
    val prev_page_url: Any?,
    val to: Int?,
    val total: Int?
)
