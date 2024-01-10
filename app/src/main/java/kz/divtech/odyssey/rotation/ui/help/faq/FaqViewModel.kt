package kz.divtech.odyssey.rotation.ui.help.faq

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.help.faq.Faq
import kz.divtech.odyssey.shared.domain.repository.FaqRepository
import javax.inject.Inject

@HiltViewModel
class FaqViewModel @Inject constructor(val repository: FaqRepository): ViewModel() {
    private val _faqResult = MutableLiveData<Resource<List<Faq>>?>()
    val faqResult: LiveData<Resource<List<Faq>>?> = _faqResult

    val refreshing = ObservableBoolean()
    val pBarVisibility = ObservableInt(View.GONE)

    fun getFaqListFromServer(){
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            val result = repository.getFaqList()
            _faqResult.value = result
            pBarVisibility.set(View.GONE)
        }
    }

    fun refreshFaqList() =
        viewModelScope.launch {
            refreshing.set(true)
            repository.getFaqList()
            refreshing.set(false)
        }
    suspend fun faqLiveData() : LiveData<List<Faq>> = repository.getFaqListFromDb().asLiveData()

    suspend fun searchFaqFromDB(searchQuery: String) = repository.searchFaqFromDb(searchQuery)

}