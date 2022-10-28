package kz.divtech.odyssey.rotation.ui.trips

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.databinding.ItemTripBinding

class TripsAdapter(private val tripList: List<Trip>, private val onTripClicked : (Trip) -> Unit) : RecyclerView.Adapter<TripsAdapter.TripViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val binding = ItemTripBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TripViewHolder(binding, onTripClicked)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        holder.bind(tripList[position])
    }

    override fun getItemCount(): Int  = tripList.size

    class TripViewHolder(val binding : ItemTripBinding, private val onTripClicked : (Trip) -> Unit) : RecyclerView.ViewHolder(binding.root){
        private lateinit var currentTrip : Trip
        init {
            binding.root.setOnClickListener{
                onTripClicked(currentTrip)
            }
        }
        fun bind(trip : Trip){
            currentTrip = trip
            binding.trip = trip
        }
    }
}