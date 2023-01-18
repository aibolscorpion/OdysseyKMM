package kz.divtech.odyssey.rotation.ui.help.press_service.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.databinding.ItemNewsBinding
import kz.divtech.odyssey.rotation.domain.model.help.press_service.news.Article

class NewsAdapter(private val newsListener: NewsListener) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    private val oldArticleList = mutableListOf<Article>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.listener = newsListener
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.article = oldArticleList[position]
    }

    override fun getItemCount() = oldArticleList.size

    fun setNews(newArticleList: List<Article>){
        val diffCallBack = NewsDiffCallBack(newArticleList, oldArticleList)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        oldArticleList.clear()
        oldArticleList.addAll(newArticleList)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root)

}

interface NewsListener{
    fun onNewsClick(articleId: Int)
}