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
import kz.divtech.odyssey.rotation.data.remote.result.asSuccess
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import java.io.File

class TermsOfAgreementViewModel: ViewModel(){
    private val _htmlMutableLiveData = MutableLiveData<String>()
    val htmlLiveData: LiveData<String> = _htmlMutableLiveData

    val progressVisibility = ObservableInt(View.GONE)

    fun getUserAgreementFromServer(){
        progressVisibility.set(View.VISIBLE)
        viewModelScope.launch {
            val response = RetrofitClient.getApiService().getUserAgreement()
            if(response.isSuccess()){
                val value = response.asSuccess().value.string()
                _htmlMutableLiveData.postValue(value)
                getFile().appendText(value)
            }
            progressVisibility.set(View.GONE)
        }
    }

    fun getFile() = File(App.appContext.externalCacheDir, Constants.TERMS_FILE_NAME)

}