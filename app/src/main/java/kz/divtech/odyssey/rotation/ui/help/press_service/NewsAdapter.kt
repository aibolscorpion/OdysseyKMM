package kz.divtech.odyssey.rotation.ui.help.press_service

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.databinding.ItemNewsBinding
import kz.divtech.odyssey.rotation.domain.model.help.press_service.News

class NewsAdapter(private val newsList : List<News>, private val onNewsClick : (News) -> Unit) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onNewsClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentNews = newsList[position]
        holder.bind(currentNews)
    }

    override fun getItemCount() = newsList.size

    class ViewHolder(val binding: ItemNewsBinding,  val onNewsClick : (News) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var currentNews : News
        init {
            binding.newsLLC.setOnClickListener {
                onNewsClick(currentNews)
            }
        }
        fun bind(news: News){
            currentNews = news
            binding.news = news
        }
    }
}