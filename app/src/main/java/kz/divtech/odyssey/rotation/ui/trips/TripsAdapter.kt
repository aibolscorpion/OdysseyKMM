package kz.divtech.odyssey.rotation.ui.trips

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.databinding.ItemTripBinding

class TripsAdapter(private val tripList: List<Trip>) : RecyclerView.Adapter<TripsAdapter.TripViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val binding = ItemTripBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TripViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        holder.binding.trip = tripList[position]
    }

    override fun getItemCount(): Int  = tripList.size

    class TripViewHolder(val binding : ItemTripBinding) : RecyclerView.ViewHolder(binding.root)
}