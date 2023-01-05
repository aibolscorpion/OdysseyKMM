package kz.divtech.odyssey.rotation.ui.help.press_service.news

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.help.press_service.news.Article
import kz.divtech.odyssey.rotation.domain.model.help.press_service.news.News
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel : ViewModel() {
    val pBarVisibility = ObservableInt(View.GONE)
    
    private val _newsMutableLiveData = MutableLiveData<List<Article>>()
    val newsLiveData: LiveData<List<Article>> = _newsMutableLiveData

    fun getAllNews(){
        pBarVisibility.set(View.VISIBLE)
        RetrofitClient.getApiService().getArticles().enqueue(object: Callback<News>{
            override fun onResponse(call: Call<News>, response: Response<News>) {
                pBarVisibility.set(View.GONE)
                when(response.code()){
                    Constants.SUCCESS_CODE -> {
                        _newsMutableLiveData.postValue(response.body()!!.data)
                    }
                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                pBarVisibility.set(View.GONE)
            }

        })
    }
}