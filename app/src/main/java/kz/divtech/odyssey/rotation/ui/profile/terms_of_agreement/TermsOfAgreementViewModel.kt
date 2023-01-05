package kz.divtech.odyssey.rotation.ui.profile.terms_of_agreement

import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TermsOfAgreementViewModel: ViewModel(){
    val content: ObservableField<String> = ObservableField("")
    val progressVisibility = ObservableInt(View.GONE)

    fun getUserAgreement(){
        progressVisibility.set(View.VISIBLE)
        RetrofitClient.getApiService().getUserAgreement().enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                progressVisibility.set(View.GONE)
                if(response.isSuccessful){
                    content.set(response.body()?.string())
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressVisibility.set(View.GONE)
            }

        })
    }

}