package kz.divtech.odyssey.rotation.ui.help.press_service.news

import androidx.recyclerview.widget.DiffUtil
import kz.divtech.odyssey.rotation.domain.model.help.press_service.news.Article

class NewsDiffCallBack(private val newArticleList: List<Article>, private val oldArticleList: List<Article>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldArticleList.size


    override fun getNewListSize(): Int = newArticleList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        newArticleList[newItemPosition].id == oldArticleList[oldItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        newArticleList[newItemPosition] == oldArticleList[oldItemPosition]
}