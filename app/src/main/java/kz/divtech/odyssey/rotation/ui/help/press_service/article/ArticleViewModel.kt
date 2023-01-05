package kz.divtech.odyssey.rotation.ui.help.press_service.article

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.help.press_service.full_article.FullArticle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleViewModel : ViewModel() {
    val pBarVisibility = ObservableInt(View.GONE)

    private val _fullArticleMutableLiveData = MutableLiveData<FullArticle>()
    val fullArticleLiveData: LiveData<FullArticle> = _fullArticleMutableLiveData

    fun getArticleById(articleId: Int){
        pBarVisibility.set(View.VISIBLE)
        RetrofitClient.getApiService().getSpecificArticleById(articleId).enqueue(object: Callback<FullArticle> {
            override fun onResponse(call: Call<FullArticle>, response: Response<FullArticle>) {
                pBarVisibility.set(View.GONE)
                when(response.code()){
                    Constants.SUCCESS_CODE -> {
                        _fullArticleMutableLiveData.postValue(response.body())
                    }
                }

            }

            override fun onFailure(call: Call<FullArticle>, t: Throwable) {
                pBarVisibility.set(View.GONE)
            }

        })
    }
}