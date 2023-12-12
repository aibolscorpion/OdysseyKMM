package kz.divtech.odyssey.rotation.ui.help.faq

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.domain.model.help.faq.Faq
import kz.divtech.odyssey.rotation.data.repository.FaqRepository
import kz.divtech.odyssey.rotation.data.remote.result.*
import javax.inject.Inject

@HiltViewModel
class FaqViewModel @Inject constructor(val repository: FaqRepository): ViewModel() {
    private val _faqResult = MutableLiveData<Result<List<Faq>>?>()
    val faqResult: LiveData<Result<List<Faq>>?> = _faqResult
    val faqLiveData : LiveData<List<Faq>> = repository.faqList.asLiveData()
    val refreshing = ObservableBoolean()
    val pBarVisibility = ObservableInt(View.GONE)

    fun getFaqListFromServer(){
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            val result = repository.getFaqListFromServer()
            _faqResult.value = result
            pBarVisibility.set(View.GONE)
        }
    }

    fun refreshFaqList() =
        viewModelScope.launch {
            refreshing.set(true)
            repository.getFaqListFromServer()
            refreshing.set(false)
        }

    suspend fun searchFaqFromDB(searchQuery: String) = repository.searchFaq(searchQuery)

}