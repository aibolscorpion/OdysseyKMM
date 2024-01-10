package kz.divtech.odyssey.rotation.ui.help.press_service.news.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.databinding.ItemNewsBinding
import kz.divtech.odyssey.shared.domain.model.help.press_service.news.Article

class NewsPagingAdapter(private val newsListener: NewsListener) : PagingDataAdapter<Article,
        NewsPagingAdapter.ViewHolder>(NewsDiffCallBack()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.listener = newsListener
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.article = getItem(position)
    }

    inner class ViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root)

}

interface NewsListener{
    fun onNewsClick(articleId: Int)
}