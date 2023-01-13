package kz.divtech.odyssey.rotation.ui.help.press_service.article

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.domain.model.help.press_service.full_article.FullArticle
import kz.divtech.odyssey.rotation.domain.repository.ArticleRepository

class ArticleViewModel(private val articleRepository: ArticleRepository) : ViewModel() {
    val pBarVisibility = ObservableInt(View.GONE)

    fun getArticleById(id: Int): LiveData<FullArticle> = articleRepository.getArticleById(id).asLiveData()

    fun getArticleByIdFromServer(id: Int) =
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            articleRepository.getArticleByIdFromServer(id)
            pBarVisibility.set(View.GONE)
        }

    fun markArticleAsRead(id: Int) =
        viewModelScope.launch {
            articleRepository.markArticleAsRead(id)
        }

    class ArticleViewModelFactory(private val articleRepository: ArticleRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(ArticleViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return ArticleViewModel(articleRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}