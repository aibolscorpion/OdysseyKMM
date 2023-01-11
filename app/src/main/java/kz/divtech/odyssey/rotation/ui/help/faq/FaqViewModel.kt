package kz.divtech.odyssey.rotation.ui.help.faq

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.domain.model.help.faq.Faq
import kz.divtech.odyssey.rotation.domain.repository.FaqRepository

class FaqViewModel(val repository: FaqRepository): ViewModel() {
    val faqLiveData : LiveData<List<Faq>> = repository.faqList.asLiveData()

    var pBarVisibility = ObservableInt(View.GONE)

    fun getFaqListFromServer() {
        pBarVisibility.set(View.VISIBLE)
        viewModelScope.launch {
            repository.getFaqListFromServer()
            pBarVisibility.set(View.GONE)
        }
    }

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