package kz.divtech.odyssey.rotation.ui.help.press_service.news

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.domain.repository.NewsRepository

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {
    val isRefreshing = ObservableBoolean()
    val pBarVisibility = ObservableInt(View.GONE)

    val newsLiveData = newsRepository.news.asLiveData()

    fun refreshNews() =
        viewModelScope.launch {
            isRefreshing.set(true)
            newsRepository.getNewsFromServer()
            isRefreshing.set(false)
        }

    fun getNewsFromServer() =
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            newsRepository.getNewsFromServer()
            pBarVisibility.set(View.GONE)
        }

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