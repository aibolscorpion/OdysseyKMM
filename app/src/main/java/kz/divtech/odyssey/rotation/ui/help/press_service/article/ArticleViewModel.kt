package kz.divtech.odyssey.rotation.ui.help.press_service.article

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.help.press_service.full_article.FullArticle
import timber.log.Timber

class ArticleViewModel : ViewModel() {
    val pBarVisibility = ObservableInt(View.GONE)

    private val _fullArticleMutableLiveData = MutableLiveData<FullArticle>()
    val fullArticleLiveData: LiveData<FullArticle> = _fullArticleMutableLiveData

    fun getArticleById(articleId: Int){
        pBarVisibility.set(View.VISIBLE)
        viewModelScope.launch {
            try{
                val response = RetrofitClient.getApiService().getSpecificArticleById(articleId)
                when(response.code()){
                    Constants.SUCCESS_CODE -> {
                        _fullArticleMutableLiveData.postValue(response.body())
                    }
                }
            }catch (e: Exception){
                Timber.e("exception - ${e.message}")
            }
            pBarVisibility.set(View.GONE)
        }
    }
}