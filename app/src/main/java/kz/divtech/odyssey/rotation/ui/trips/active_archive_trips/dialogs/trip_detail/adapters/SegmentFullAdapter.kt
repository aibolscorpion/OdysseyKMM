package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.common.Constants
import kz.divtech.odyssey.rotation.databinding.ItemSegmentFullBinding
import kz.divtech.odyssey.shared.domain.model.trips.SegmentStatus
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.BindingAdapter.setSpannedText
import kz.divtech.odyssey.rotation.common.utils.LocalDateTimeUtils.getLocalDateTimeByPattern
import kz.divtech.odyssey.rotation.common.utils.Utils.getRefundSegmentStatus
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Segment
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Trip
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class SegmentFullAdapter(val context: Context) : RecyclerView.Adapter<SegmentFullAdapter.TicketViewHolder>() {
    private val listOfSegments = mutableListOf<Segment>()
    var trip: Trip? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val binding = ItemSegmentFullBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TicketViewHolder(binding, listOfSegments)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        holder.bind(listOfSegments[position])
    }

    override fun getItemCount() = listOfSegments.size

    fun setSegmentList(trip: Trip){
        this.trip = trip
        listOfSegments.clear()
        listOfSegments.addAll(trip.segments)
    }

    inner class TicketViewHolder(val binding: ItemSegmentFullBinding, private val segmentList: List<Segment>) : RecyclerView.ViewHolder(binding.root){

        fun bind(segment: Segment){
            if(segment.train != null){
                setTrainInfo(segment)
            }else{
                setNoTrainInfo(segment)
            }
        }

        private fun setTrainInfo(segment: Segment){
            binding.segment = segment
            binding.refundSegmentStatus =
                trip?.refundApplications?.let { getRefundSegmentStatus(it, segment.id) }
            binding.inWayTimeTV.text = parseMinutesToTime(segment.train?.inWayMinutes)
            setViewsByStatus(defineSegmentStatus(segment), segment)
            binding.trainTransferTV.apply {
                if(segmentList.size > absoluteAdapterPosition+1) {
                    visibility = View.VISIBLE
                    setSpannedText(this, getWaitingTime(segmentList, absoluteAdapterPosition))
                } else {
                    visibility = View.GONE
                }
            }
        }
        private fun setNoTrainInfo(segment: Segment){
            binding.titleTV.text = context.getString(R.string.no_train_info_title,
                segment.depStationName, segment.arrStationName)
            binding.noSegmentInfoCL.isVisible = true
            binding.fullSegmentInfoCL.isVisible = false
        }

        private fun defineSegmentStatus(segment: Segment): SegmentStatus {
            lateinit var segmentStatus: SegmentStatus
            when(segment.status){
                Constants.STATUS_OPENED -> {
                    segmentStatus = if(segment.activeProcess.equals(Constants.WATCHING)){
                        SegmentStatus.ON_THE_WAITING_LIST
                    }else{
                        SegmentStatus.OPENED
                    }
                }
                Constants.STATUS_RETURNED -> segmentStatus = SegmentStatus.RETURNED
                Constants.STATUS_CANCELED -> segmentStatus = SegmentStatus.CANCELED
                Constants.STATUS_ISSUED -> segmentStatus = SegmentStatus.ISSUED
            }
            return segmentStatus
        }

        private fun setViewsByStatus(segmentStatus: SegmentStatus, segment: Segment){
            when(segmentStatus){

                SegmentStatus.OPENED ->  {
                    val color = context.getColor(R.color.returned_ticket_text)
                    val text = context.getString(R.string.tickets_are_not_purchased_2)
                    setCarriageAndPlaceNumber(color, text, text)
                    setDirectionColor(color, R.drawable.icon_train_grey,
                        R.drawable.icon_point_grey)
                }

                SegmentStatus.ON_THE_WAITING_LIST -> {
                    val textColor = context.getColor(R.color.on_the_waiting_list_text)
                    val color = context.getColor(R.color.on_the_waiting_list_bg)
                    val text =  context.getString(R.string.tickets_are_on_the_waiting_list)
                    setCarriageAndPlaceNumber(textColor, text, text)
                    setDirectionColor(color, R.drawable.icon_train_orange,
                        R.drawable.icon_point_orange)
                }

                SegmentStatus.CANCELED, SegmentStatus.RETURNED -> {
                    val textColor = context.getColor(R.color.returned_ticket_text)
                    val color = context.getColor(R.color.returned_bg)
                    setCarriageAndPlaceNumber(textColor, segment.ticket?.carNumber,
                        segment.ticket?.seatNumber)
                    setDirectionColor(color, R.drawable.icon_train_red,
                        R.drawable.icon_point_red)

                    val listOfTextViews = listOf(binding.departureDateTV, binding.departureTimeTV,
                        binding.inWayTimeTV, binding.trainNumberTV, binding.arrivalDateTV,
                        binding.arrivalTimeTV)

                    listOfTextViews.forEach{ textView -> textView.setTextColor(textColor) }
                }

                SegmentStatus.ISSUED -> {
                    val color = context.getColor(R.color.black)
                    val imageColor = context.getColor(R.color.issued_bg)
                    setCarriageAndPlaceNumber(color, segment.ticket?.carNumber,
                        segment.ticket?.seatNumber)
                    setDirectionColor(imageColor, R.drawable.icon_train_green,
                        R.drawable.icon_point_green)
                }
            }
        }

        private fun setCarriageAndPlaceNumber(color: Int, carNumber: String?, placeNumber: String?){
            binding.carriageNumberTV.apply {
                text = carNumber
                setTextColor(color)
            }

            binding.placeNumberTV.apply {
                text = placeNumber
                setTextColor(color)
            }
        }

        private fun setDirectionColor(color: Int, departureIcon: Int, destinationIcon: Int){
            binding.departureIV.setImageResource(departureIcon)
            binding.verticalLineV.setBackgroundColor(color)
            binding.destinationIV.setImageResource(destinationIcon)
        }

        private fun getWaitingTime(segmentList: List<Segment>, position: Int): String{
            return if(segmentList[position+1].train != null){
                val arrLocalDateTime = segmentList[position].train?.arrDateTime?.getLocalDateTimeByPattern()
                val depLocalDateTime = segmentList[position+1].train?.depDateTime?.getLocalDateTimeByPattern()
                var tempDateTime: LocalDateTime = LocalDateTime.from(arrLocalDateTime)

                val hours = tempDateTime.until(depLocalDateTime, ChronoUnit.HOURS)
                tempDateTime = tempDateTime.plusHours(hours)
                val minutes = tempDateTime.until(depLocalDateTime, ChronoUnit.MINUTES)
                context.getString(R.string.train_transfer_with_time,
                    segmentList[position].arrStationName, hours, minutes)
            }else{
                context.getString(R.string.train_transfer_without_time,
                    segmentList[position].arrStationName)
            }
        }

        private fun parseMinutesToTime(inWayMinutes: Int?): String{
            val hours = inWayMinutes?.div(60)
            val minutes = inWayMinutes?.rem(60)
            return context.getString(R.string.in_way_time, hours, minutes)
        }

    }

}