package kz.divtech.odyssey.rotation.ui.help.press_service.news

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.shared.domain.model.help.press_service.news.Article
import kz.divtech.odyssey.shared.domain.repository.NewsRepository
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val newsRepository: NewsRepository) : ViewModel() {

    fun getPagingNews(): Flow<PagingData<Article>> =
        newsRepository.getPagingNews().cachedIn(viewModelScope)

    suspend fun searchNewsFromDB(searchQuery: String) =
        newsRepository.searchNewsFromDb(searchQuery)

}