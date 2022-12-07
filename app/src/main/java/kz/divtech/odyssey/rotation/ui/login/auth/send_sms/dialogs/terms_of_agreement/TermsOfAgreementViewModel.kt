package kz.divtech.odyssey.rotation.ui.login.auth.send_sms.dialogs.terms_of_agreement

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import kz.divtech.odyssey.rotation.data.remote.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TermsOfAgreementViewModel: ViewModel(){
    val content: ObservableField<String> = ObservableField("")
    val progressVisibility = ObservableBoolean(true)

    fun getUserAgreement(){
        RetrofitClient.getApiService().getUserAgreement().enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                progressVisibility.set(false)
                if(response.isSuccessful){
                    content.set(response.body()?.string())
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressVisibility.set(false)
            }

        })
    }

}