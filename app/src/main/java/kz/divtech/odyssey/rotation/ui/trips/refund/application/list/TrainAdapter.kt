package kz.divtech.odyssey.rotation.ui.trips.refund.application.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.databinding.ItemTrainBinding
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Segment

class TrainAdapter : RecyclerView.Adapter<TrainAdapter.TicketViewHolder>() {
    private val oldSegmentList = mutableListOf<Segment>()

    fun setSegmentList(newSegmentList: List<Segment>){
        oldSegmentList.clear()
        oldSegmentList.addAll(newSegmentList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val binding = ItemTrainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TicketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        holder.binding.train = oldSegmentList[position].train
    }

    override fun getItemCount() = oldSegmentList.size


    inner class TicketViewHolder(val binding: ItemTrainBinding): RecyclerView.ViewHolder(binding.root)

}