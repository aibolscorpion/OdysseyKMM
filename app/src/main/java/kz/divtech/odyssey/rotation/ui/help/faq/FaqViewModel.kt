package kz.divtech.odyssey.rotation.ui.help.faq

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.help.faq.Faq
import timber.log.Timber

class FaqViewModel: ViewModel() {
    private val _faqList = MutableLiveData<List<Faq>>()
    val faqList : LiveData<List<Faq>> = _faqList

    var pBarVisibility = ObservableInt(View.GONE)

    fun getFaqList() {
        pBarVisibility.set(View.VISIBLE)
        viewModelScope.launch {
            try{
                val response = RetrofitClient.getApiService().getFAQs()
                if(response.isSuccessful){
                    _faqList.postValue(response.body()!!)
                }
            }catch (e: Exception){
                Timber.e("exception - ${e.message}")
            }
            pBarVisibility.set(View.GONE)
        }

    }
}