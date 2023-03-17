package kz.divtech.odyssey.rotation.ui.trips.refund.application_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.databinding.ItemRefundBinding

class RefundListAdapter(val btnClick: RefundBtnClick) : RecyclerView.Adapter<RefundListAdapter.ApplicationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationViewHolder {
        val binding = ItemRefundBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ApplicationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ApplicationViewHolder, position: Int) {
        holder.binding.cancelRefundBtn.setOnClickListener {
            btnClick.onCancelClick(1)
        }
        holder.binding.detailRefundBtn.setOnClickListener {
            btnClick.onDetailClick(1)
        }
    }

    override fun getItemCount()  = 3


    inner class ApplicationViewHolder(val binding: ItemRefundBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface RefundBtnClick{
        fun onDetailClick(position: Int)
        fun onCancelClick(position: Int)
    }
}