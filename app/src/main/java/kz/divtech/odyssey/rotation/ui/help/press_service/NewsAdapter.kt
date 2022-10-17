package kz.divtech.odyssey.rotation.ui.help.press_service

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.databinding.ItemNewsBinding

class NewsAdapter(private val newsList : List<News>, private val onNewsClickListener : OnClickNewsListener) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentNews = newsList[position]
        holder.binding.news = currentNews
        holder.binding.newsLLC.setOnClickListener {
            onNewsClickListener.onClick(currentNews)
        }
    }

    override fun getItemCount() = newsList.size

    class ViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}