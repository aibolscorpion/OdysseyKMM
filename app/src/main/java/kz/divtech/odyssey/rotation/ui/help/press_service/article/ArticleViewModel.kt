package kz.divtech.odyssey.rotation.ui.help.press_service.article

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.help.press_service.article.FullArticle
import kz.divtech.odyssey.shared.domain.model.help.press_service.article.FullArticleResponse
import kz.divtech.odyssey.shared.domain.repository.ArticleRepository
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(private val articleRepository: ArticleRepository) : ViewModel() {
    val pBarVisibility = ObservableInt(View.GONE)

    private val _articleResult = MutableLiveData<Resource<FullArticleResponse>>()
    val articleResult: LiveData<Resource<FullArticleResponse>> = _articleResult

    suspend fun getArticleById(id: Int): LiveData<FullArticle?> = articleRepository.getArticleFromDbById(id).asLiveData()

    fun getArticleByIdFromServer(id: Int) =
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            val response = articleRepository.getArticleById(id)
            _articleResult.value = response
            pBarVisibility.set(View.GONE)
        }

    fun markArticleAsRead(id: Int) =
        viewModelScope.launch {
            articleRepository.markArticleAsRead(id)
        }


}