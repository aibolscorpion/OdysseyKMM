package kz.divtech.odyssey.rotation.ui.profile.notification.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.databinding.ItemLoadAdapterBinding

class LoaderAdapter(private val callback: RetryCallback) : LoadStateAdapter<LoaderAdapter.LoaderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoaderViewHolder {
        val binding = ItemLoadAdapterBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        binding.callback = callback
        return LoaderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoaderViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class LoaderViewHolder(val binding: ItemLoadAdapterBinding) :
        RecyclerView.ViewHolder(binding.root){

            fun bind(loadState: LoadState){
                binding.loadProgressB.isVisible = loadState is LoadState.Loading
                binding.retryBtn.isVisible = loadState is LoadState.Error
            }
    }

    interface RetryCallback{
        fun onRetryClicked()
    }

}