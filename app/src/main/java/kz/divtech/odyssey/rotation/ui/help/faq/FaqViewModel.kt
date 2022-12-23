package kz.divtech.odyssey.rotation.ui.help.faq

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.divtech.odyssey.rotation.data.remote.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.help.faq.Faq
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FaqViewModel: ViewModel() {
    private val _faqList = MutableLiveData<List<Faq>>()
    val faqList : LiveData<List<Faq>> = _faqList

    var pBarVisibility = ObservableInt(View.GONE)

    fun getFaqList() {
        pBarVisibility.set(View.VISIBLE)
        RetrofitClient.getApiService().getFAQs().enqueue(object : Callback<List<Faq>>{
            override fun onResponse(call: Call<List<Faq>>, response: Response<List<Faq>>) {
                pBarVisibility.set(View.GONE)
                if(response.isSuccessful){
                    _faqList.postValue(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<Faq>>, t: Throwable) {
                pBarVisibility.set(View.GONE)
            }

        })
    }
}