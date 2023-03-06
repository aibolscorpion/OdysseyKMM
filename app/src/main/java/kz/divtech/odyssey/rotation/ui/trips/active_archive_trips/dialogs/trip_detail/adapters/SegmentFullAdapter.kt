package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.databinding.ItemSegmentFullBinding
import kz.divtech.odyssey.rotation.domain.model.trips.Segment
import kz.divtech.odyssey.rotation.domain.model.trips.SegmentStatus
import kz.divtech.odyssey.rotation.utils.LocalDateTimeUtils.getLocalDateTimeByPattern
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class SegmentFullAdapter : RecyclerView.Adapter<SegmentFullAdapter.TicketViewHolder>() {
    private val listOfSegments = mutableListOf<Segment>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val binding = ItemSegmentFullBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TicketViewHolder(binding, listOfSegments)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {

        holder.bind(listOfSegments[position], position)
    }

    override fun getItemCount() = listOfSegments.size

    fun setSegmentList(segmentList: List<Segment>?){
        listOfSegments.clear()
        segmentList?.let { listOfSegments.addAll(it) }
    }

    class TicketViewHolder(val binding: ItemSegmentFullBinding, private val segmentList: List<Segment>) : RecyclerView.ViewHolder(binding.root){
        private lateinit var currentSegment: Segment

        fun bind(segment: Segment, position: Int){
            currentSegment = segment
            binding.segment = currentSegment

            binding.inWayTimeTV.text = parseMinutesToTime(segment.train?.in_way_minutes)
            setViewsByStatus(defineSegmentStatus(segment))

            binding.trainTransferTV.apply {
                if(segmentList.size > position+1) {
                    visibility = View.VISIBLE
                    text = calculateWaitingTime(segmentList, position)
                } else {
                    visibility = View.GONE
                }
            }
        }

        private fun defineSegmentStatus(segment: Segment): SegmentStatus {
            lateinit var segmentStatus: SegmentStatus
            when(segment.status){
                Constants.STATUS_OPENED -> {
                    segmentStatus = if(currentSegment.active_process.equals(Constants.WATCHING)){
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

        private fun setViewsByStatus(segmentStatus: SegmentStatus){
            when(segmentStatus){

                SegmentStatus.OPENED ->  {
                    val color = App.appContext.getColor(R.color.returned_ticket_text)
                    val text = App.appContext.getString(R.string.tickets_are_not_purchased_2)
                    setCarriageAndPlaceNumber(color, text, text)
                    setDirectionColor(color, R.drawable.icon_train_grey,
                        R.drawable.icon_point_grey)
                }

                SegmentStatus.ON_THE_WAITING_LIST -> {
                    val textColor = App.appContext.getColor(R.color.on_the_waiting_list_text)
                    val color = App.appContext.getColor(R.color.on_the_waiting_list_bg)
                    val text =  App.appContext.getString(R.string.tickets_are_on_the_waiting_list)
                    setCarriageAndPlaceNumber(textColor, text, text)
                    setDirectionColor(color, R.drawable.icon_train_orange,
                        R.drawable.icon_point_orange)
                }

                SegmentStatus.CANCELED, SegmentStatus.RETURNED -> {
                    val textColor = App.appContext.getColor(R.color.returned_ticket_text)
                    val color = App.appContext.getColor(R.color.returned_bg)
                    setCarriageAndPlaceNumber(textColor, currentSegment.ticket?.car_number,
                        currentSegment.ticket?.seat_number)
                    setDirectionColor(color, R.drawable.icon_train_red,
                        R.drawable.icon_point_red)

                    val listOfTextViews = listOf(binding.departureDateTV, binding.departureTimeTV,
                        binding.inWayTimeTV, binding.trainNumberTV, binding.arrivalDateTV,
                        binding.arrivalTimeTV)

                    listOfTextViews.forEach{ textView -> textView.setTextColor(textColor) }
                }

                SegmentStatus.ISSUED -> {
                    val color = App.appContext.getColor(R.color.black)
                    val imageColor = App.appContext.getColor(R.color.issued_bg)
                    setCarriageAndPlaceNumber(color, currentSegment.ticket?.car_number,
                        currentSegment.ticket?.seat_number)
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

        private fun calculateWaitingTime(segmentList: List<Segment>, position: Int): String{
            val arrLocalDateTime = getLocalDateTimeByPattern(segmentList[position].train?.arr_date_time!!)
            val depLocalDateTime = getLocalDateTimeByPattern(segmentList[position+1].train?.dep_date_time!!)
            var tempDateTime: LocalDateTime = LocalDateTime.from(arrLocalDateTime)

            val hours: Long = tempDateTime.until(depLocalDateTime, ChronoUnit.HOURS)
            tempDateTime = tempDateTime.plusHours(hours)
            val minutes: Long = tempDateTime.until(depLocalDateTime, ChronoUnit.MINUTES)

            return App.appContext.getString(R.string.train_transfer,
                segmentList[position].arr_station, hours, minutes)
        }

        private fun parseMinutesToTime(inWayMinutes: Int?): String{
            val hours = inWayMinutes!!/60
            val minutes = inWayMinutes%60
            return App.appContext.getString(R.string.in_way_time, hours, minutes)
        }

    }

}