package kz.divtech.odyssey.rotation.ui.help.faq

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.domain.model.help.faq.Faq
import kz.divtech.odyssey.rotation.domain.repository.FaqRepository

class FaqViewModel(val repository: FaqRepository): ViewModel() {
    val faqLiveData : LiveData<List<Faq>> = repository.faqList.asLiveData()

    val refreshing = ObservableBoolean()
    val pBarVisibility = ObservableInt(View.GONE)

    fun getFaqListFromServer() =
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            repository.getFaqListFromServer(isRefreshing = false)
            pBarVisibility.set(View.GONE)
        }

    fun refreshFaqList() =
        viewModelScope.launch {
            refreshing.set(true)
            repository.getFaqListFromServer(isRefreshing = true)
            refreshing.set(false)
        }

    fun searchFaqFromDB(searchQuery: String): LiveData<List<Faq>> =
        repository.searchFaq(searchQuery).asLiveData()


    class FaqViewModelFactory(val repository: FaqRepository): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(FaqViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return FaqViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown viewModel class")
        }
    }
}