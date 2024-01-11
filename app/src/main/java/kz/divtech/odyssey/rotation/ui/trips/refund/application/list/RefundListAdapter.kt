package kz.divtech.odyssey.rotation.ui.trips.refund.application.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.databinding.ItemRefundBinding
import kz.divtech.odyssey.shared.domain.model.trips.refund.applications.RefundAppItem

class RefundListAdapter(private val btnClick: RefundBtnClick) : RecyclerView.Adapter<RefundListAdapter.ApplicationViewHolder>() {
    private val oldRefundList = mutableListOf<RefundAppItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationViewHolder {
        val binding = ItemRefundBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ApplicationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ApplicationViewHolder, position: Int) {
        val refundAppItem = oldRefundList[position]
        holder.bind(refundAppItem)
    }

    override fun getItemCount()  = oldRefundList.size

    fun setRefundList(newRefundList: List<RefundAppItem>){
        oldRefundList.clear()
        oldRefundList.addAll(newRefundList)
        notifyDataSetChanged()
    }

    inner class ApplicationViewHolder(val binding: ItemRefundBinding) : RecyclerView.ViewHolder(binding.root) {
        val adapter = TrainAdapter()
        private var refundAppItem: RefundAppItem? = null

        init{
           binding.refundSegmentsRV.adapter = adapter

            binding.cancelRefundBtn.setOnClickListener {
                btnClick.onCancelClick(refundAppItem!!.id)
            }
            binding.detailRefundBtn.setOnClickListener {
                btnClick.onDetailClick(refundAppItem!!)
            }
       }

        fun bind(refundAppItem: RefundAppItem){
            this.refundAppItem = refundAppItem
            binding.refundApp = refundAppItem
            adapter.setSegmentList(refundAppItem.realSegment!!.toList())
        }
    }

    interface RefundBtnClick{
        fun onDetailClick(refundAppItem: RefundAppItem)
        fun onCancelClick(refundId: Int)
    }

}