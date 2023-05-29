package kz.divtech.odyssey.rotation.ui.profile.terms_of_agreement

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.data.remote.result.*
import okhttp3.ResponseBody
import java.io.File

class TermsOfAgreementViewModel: ViewModel(){
    private val _result = MutableLiveData<Result<ResponseBody>>()
    val result: LiveData<Result<ResponseBody>> = _result

    val progressVisibility = ObservableInt(View.GONE)

    fun getUserAgreementFromServer(){
        viewModelScope.launch {
            progressVisibility.set(View.VISIBLE)
            val response = RetrofitClient.getApiService().getUserAgreement()
            _result.value = response
            progressVisibility.set(View.GONE)
        }
    }

    fun getFile() = File(App.appContext.externalCacheDir, Constants.TERMS_FILE_NAME)

}