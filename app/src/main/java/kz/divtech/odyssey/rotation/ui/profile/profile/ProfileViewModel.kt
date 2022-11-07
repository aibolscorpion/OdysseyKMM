package kz.divtech.odyssey.rotation.ui.profile.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.divtech.odyssey.rotation.api.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class ProfileViewModel: ViewModel() {
    private val _isSuccessfullyLoggedOut = MutableLiveData<Boolean>()
    val isSuccessfullyLoggedOut = _isSuccessfullyLoggedOut

    fun logout(){
        RetrofitClient.getApiService().logout().enqueue(object: retrofit2.Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                _isSuccessfullyLoggedOut.postValue(response.isSuccessful)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                _isSuccessfullyLoggedOut.postValue(false)

            }
        })

    }
}