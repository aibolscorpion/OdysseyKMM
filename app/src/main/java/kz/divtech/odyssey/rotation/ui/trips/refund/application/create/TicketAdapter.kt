package kz.divtech.odyssey.rotation.ui.trips.refund.application.create

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.databinding.ItemTicketBinding
import kz.divtech.odyssey.rotation.domain.model.trips.Segment

class TicketAdapter(private val checkListener: OnItemCheckListener) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {
    private val segmentList = mutableListOf<Segment>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val binding = ItemTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TicketViewHolder(binding)
    }

    fun setTicketList(newSegmentList: List<Segment>){
        segmentList.clear()
        segmentList.addAll(newSegmentList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val segment = segmentList[position]
        val checkBox = holder.binding.ticketCB
        holder.binding.ticket = segment.ticket
        holder.binding.root.setOnClickListener {
            checkBox.isChecked = !checkBox.isChecked
            if(checkBox.isChecked){
                checkListener.onItemCheck(segment.id)
            }else{
                checkListener.onItemUncheck(segment.id)
            }
        }

        if(segment.status == Constants.REFUND_STATUS_PENDING){
            holder.binding.root.isEnabled = false
            holder.binding.ticketCB.isEnabled = false
            holder.binding.ticketLL.setBackgroundColor(App.appContext.getColor(R.color.disabled_ticket))
            holder.binding.depArrNameTV.setTextViewColor(R.color.black_opacity_30)
            holder.binding.trainInfoTV.setTextViewColor(R.color.disabled_dep_arr_name)
            holder.binding.ticketDateTimeTV.setTextViewColor(R.color.disabled_dep_arr_name)
            holder.binding.ticketInfoTV.setTextViewColor(R.color.black_opacity_30)
            holder.binding.ticketNumberTV.setTextViewColor(R.color.black_opacity_30)
        }
    }

    private fun TextView.setTextViewColor(color: Int){
        this.setTextColor(App.appContext.getColor(color))
    }

    override fun getItemCount() = segmentList.size

    inner class TicketViewHolder(val binding: ItemTicketBinding)
        : RecyclerView.ViewHolder(binding.root)

    interface OnItemCheckListener{
        fun onItemCheck(segmentId: Int)
        fun onItemUncheck(segmentId: Int)
    }

}
