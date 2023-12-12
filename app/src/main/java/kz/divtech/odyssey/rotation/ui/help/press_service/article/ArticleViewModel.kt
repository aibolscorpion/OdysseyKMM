package kz.divtech.odyssey.rotation.ui.help.press_service.article

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.domain.model.help.press_service.full_article.FullArticle
import kz.divtech.odyssey.rotation.data.repository.ArticleRepository
import kz.divtech.odyssey.rotation.data.remote.result.*
import kz.divtech.odyssey.rotation.domain.model.help.press_service.full_article.FullArticleResponse
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(private val articleRepository: ArticleRepository) : ViewModel() {
    val pBarVisibility = ObservableInt(View.GONE)

    private val _articleResult = MutableLiveData<Result<FullArticleResponse>>()
    val articleResult: LiveData<Result<FullArticleResponse>> = _articleResult

    fun getArticleById(id: Int): LiveData<FullArticle> = articleRepository.getArticleById(id).asLiveData()

    fun getArticleByIdFromServer(id: Int) =
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            val response = articleRepository.getArticleByIdFromServer(id)
            _articleResult.value = response
            pBarVisibility.set(View.GONE)
        }

    fun markArticleAsRead(id: Int) =
        viewModelScope.launch {
            articleRepository.markArticleAsRead(id)
        }


}