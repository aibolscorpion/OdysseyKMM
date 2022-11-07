package kz.divtech.odyssey.rotation.ui.help.faq

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.divtech.odyssey.rotation.data.remote.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.help.faq.Faq
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FaqViewModel: ViewModel() {
    private val _faqList = MutableLiveData<ArrayList<Faq>>()
    val faqList : LiveData<ArrayList<Faq>> = _faqList

    fun getFaqList() {
        RetrofitClient.getApiService().getFAQs().enqueue(object : Callback<ArrayList<Faq>>{
            override fun onResponse(call: Call<ArrayList<Faq>>, response: Response<ArrayList<Faq>>) {
                if(response.isSuccessful){
                    _faqList.postValue(response.body()!!)
                }
            }

            override fun onFailure(call: Call<ArrayList<Faq>>, t: Throwable) {

            }

        })
    }
}