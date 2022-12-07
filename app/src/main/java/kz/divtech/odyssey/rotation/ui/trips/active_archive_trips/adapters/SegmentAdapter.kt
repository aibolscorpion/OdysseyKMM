package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kz.divtech.odyssey.rotation.databinding.ItemSegmentShortBinding
import kz.divtech.odyssey.rotation.domain.model.trips.Segment

class SegmentAdapter : Adapter<SegmentAdapter.SegmentViewHolder>() {
    private val listOfSegments = ArrayList<Segment>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SegmentViewHolder {
        val binding = ItemSegmentShortBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SegmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SegmentViewHolder, position: Int) {
        holder.binding.segments = listOfSegments[position]
    }

    override fun getItemCount() = listOfSegments.size

    fun setSegmentList(segmentList: List<Segment>?){
        listOfSegments.clear()
        segmentList?.let { listOfSegments.addAll(it) }
        notifyDataSetChanged()
    }

    class SegmentViewHolder(val binding: ItemSegmentShortBinding) : ViewHolder(binding.root)

}