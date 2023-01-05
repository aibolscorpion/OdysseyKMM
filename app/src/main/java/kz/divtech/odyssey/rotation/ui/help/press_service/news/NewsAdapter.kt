package kz.divtech.odyssey.rotation.ui.help.press_service.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.databinding.ItemNewsBinding
import kz.divtech.odyssey.rotation.domain.model.help.press_service.news.Article

class NewsAdapter(val newsListener: NewsListener) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    private val news = mutableListOf<Article>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(news[position])
    }

    override fun getItemCount() = news.size

    fun setNews(articleList: List<Article>){
        news.clear()
        news.addAll(articleList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var currentArticle : Article
        init {
            binding.articleLLC.setOnClickListener {
                newsListener.onNewsClick(currentArticle.id)
            }
        }
        fun bind(article: Article){
            currentArticle = article
            binding.article = article
        }
    }

}

interface NewsListener{
    fun onNewsClick(articleId: Int)
}