package kz.divtech.odyssey.rotation.ui.help.press_service.news

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.help.press_service.news.Article
import timber.log.Timber

class NewsViewModel : ViewModel() {
    val pBarVisibility = ObservableInt(View.GONE)
    
    private val _newsMutableLiveData = MutableLiveData<List<Article>>()
    val newsLiveData: LiveData<List<Article>> = _newsMutableLiveData

    fun getAllNews(){
        pBarVisibility.set(View.VISIBLE)

        viewModelScope.launch {
            try{
                val response = RetrofitClient.getApiService().getArticles()
                when(response.code()){
                    Constants.SUCCESS_CODE -> {
                        _newsMutableLiveData.postValue(response.body()!!.data)
                    }
                }
            }catch (e: Exception){
                Timber.e("exception - ${e.message}")
            }
            pBarVisibility.set(View.GONE)
        }
    }

    fun markAsRead(id: Int){
        pBarVisibility.set(View.VISIBLE)
        viewModelScope.launch {
            try{
                RetrofitClient.getApiService().markAsReadArticleById(id)
            }catch (e: Exception){
                Timber.e("exception - ${e.message}")
            }
            pBarVisibility.set(View.GONE)
        }
    }
}