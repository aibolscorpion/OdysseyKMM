package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.databinding.ItemTripBinding
import kz.divtech.odyssey.rotation.domain.model.trips.Trip

class TripsAdapter(private val onTripListener: OnTripListener) : RecyclerView.Adapter<TripsAdapter.TripViewHolder>() {
    private val oldTripList = mutableListOf<Trip>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val binding = ItemTripBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.listener = onTripListener
        return TripViewHolder(binding)
    }

    fun setTripList(newTripsList: List<Trip>){
        val diffCallBack = TripsDiffCallBack(newTripsList, oldTripList)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        oldTripList.clear()
        oldTripList.addAll(newTripsList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        holder.bind(oldTripList[position])
    }

    override fun getItemCount(): Int  = oldTripList.size

    inner class TripViewHolder (val binding : ItemTripBinding) : RecyclerView.ViewHolder(binding.root){
        private lateinit var currentTrip : Trip
        private val adapter = SegmentAdapter()

        init {
            binding.segmentsRV.adapter = adapter
        }

        fun bind(trip : Trip){
            binding.trip = trip
            currentTrip = trip
            adapter.setSegmentList(currentTrip.segments)
        }

    }

    interface OnTripListener {
        fun onTripClicked(trip: Trip)
    }

}