package kz.divtech.odyssey.rotation.ui.help.press_service.news

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.domain.model.help.press_service.news.Article
import kz.divtech.odyssey.rotation.domain.repository.NewsRepository

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    fun getPagingNews(): Flow<PagingData<Article>> =
        newsRepository.getPagingNews().cachedIn(viewModelScope)

    suspend fun searchNewsFromDB(searchQuery: String) =
        newsRepository.searchArticlesFromDB(searchQuery)

    class ViewModelFactory(private val newsRepository: NewsRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(NewsViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return NewsViewModel(newsRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}