package kz.divtech.odyssey.rotation.ui.login.update_phone

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.login.update_phone.UpdatePhoneRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class UpdatePhoneViewModel(app: Application) : AndroidViewModel(app) {
    private val _isApplicationSent = MutableLiveData<Boolean>()
    val isApplicationSent = _isApplicationSent

    private val _message = MutableLiveData<String>()
    val message = _message

    fun updatePhoneNumber(request: UpdatePhoneRequest){
        RetrofitClient.getApiService().updatePhoneNumber(request).enqueue(object: retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                when(response.code()){
                    200 -> _isApplicationSent.postValue(true)
                    422 -> {
                        val message = getApplication<Application>().getString(R.string.invalid_format_phone_number)
                        _message.postValue(message)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                _isApplicationSent.postValue(false)
            }

        })
    }
}