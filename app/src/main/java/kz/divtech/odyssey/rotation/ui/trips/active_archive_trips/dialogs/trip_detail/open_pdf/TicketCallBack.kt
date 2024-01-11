package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail.open_pdf

import androidx.recyclerview.widget.DiffUtil
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Ticket

class TicketCallBack(private val oldTicketList: List<Ticket>, private val  newTicketList: List<Ticket>) : DiffUtil.Callback() {
    override fun getOldListSize() = oldTicketList.size

    override fun getNewListSize() = newTicketList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
         oldTicketList[oldItemPosition].id == newTicketList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldTicketList[oldItemPosition] == newTicketList[newItemPosition]
}