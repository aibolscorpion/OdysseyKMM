package kz.divtech.odyssey.rotation.ui.help.faq

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.domain.model.help.faq.Faq
import kz.divtech.odyssey.rotation.domain.repository.FaqRepository
import kz.divtech.odyssey.rotation.data.remote.result.*

class FaqViewModel(val repository: FaqRepository): ViewModel() {
    private val _faqResult = MutableLiveData<Result<List<Faq>>?>()
    val faqResult: LiveData<Result<List<Faq>>?> = _faqResult
    val faqLiveData : LiveData<List<Faq>> = repository.faqList.asLiveData()
    val refreshing = ObservableBoolean()
    val pBarVisibility = ObservableInt(View.GONE)

    fun getFaqListFromServer(){
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            val result = repository.getFaqListFromServer(isRefreshing = false)
            _faqResult.value = result
            pBarVisibility.set(View.GONE)
        }
    }

    fun refreshFaqList() =
        viewModelScope.launch {
            refreshing.set(true)
            repository.getFaqListFromServer(isRefreshing = true)
            refreshing.set(false)
        }

    suspend fun searchFaqFromDB(searchQuery: String) = repository.searchFaq(searchQuery)


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