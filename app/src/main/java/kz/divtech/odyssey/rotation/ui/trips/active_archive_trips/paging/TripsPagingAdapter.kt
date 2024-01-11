package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.databinding.ItemTripBinding
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.SegmentAdapter
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Trip

class TripsPagingAdapter(private val onTripListener: OnTripListener) : PagingDataAdapter<Trip, TripsPagingAdapter.TripViewHolder>(
    TripsDiffCallBack()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val binding = ItemTripBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        binding.listener = onTripListener
        return TripViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TripViewHolder (val binding : ItemTripBinding) : RecyclerView.ViewHolder(binding.root){
        private val adapter = SegmentAdapter()

        init {
            binding.segmentsRV.adapter = adapter
        }

        fun bind(trip : Trip?){
            binding.trip = trip
            adapter.setSegmentList(trip)
        }

    }

    interface OnTripListener {
        fun onTripClicked(trip: Trip?)
    }

}