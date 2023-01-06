package kz.divtech.odyssey.rotation.ui.profile.terms_of_agreement

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TermsOfAgreementViewModel: ViewModel(){
    private val _htmlMutableLiveData = MutableLiveData<String>()
    val htmlLiveData: LiveData<String> = _htmlMutableLiveData

    val progressVisibility = ObservableInt(View.GONE)

    fun getUserAgreement(){
        progressVisibility.set(View.VISIBLE)
        RetrofitClient.getApiService().getUserAgreement().enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful){
                    _htmlMutableLiveData.postValue(response.body()?.string())
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressVisibility.set(View.GONE)
            }

        })
    }

}