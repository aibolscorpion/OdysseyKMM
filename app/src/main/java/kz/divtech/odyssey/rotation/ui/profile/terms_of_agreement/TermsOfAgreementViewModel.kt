package kz.divtech.odyssey.rotation.ui.profile.terms_of_agreement

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.data.remote.result.*
import java.io.File

class TermsOfAgreementViewModel: ViewModel(){
    private val _failureResult = MutableLiveData<Throwable>()
    val failureResult: LiveData<Throwable> = _failureResult

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text

    val progressVisibility = ObservableInt(View.GONE)

    fun getUserAgreementFromServer(){
        progressVisibility.set(View.VISIBLE)
        viewModelScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.getApiService().getUserAgreement()
            if(response.isSuccess()){
                writeFile(response.asSuccess().value.string())
                readFile()
            }else{
                response.asFailure().error?.let {
                    _failureResult.postValue(it)
                }
            }
            withContext(Dispatchers.Main){
                progressVisibility.set(View.GONE)
            }
        }
    }

    suspend fun readFile(){
        withContext(Dispatchers.IO){
            _text.postValue(getFile().readText())
        }
    }

    private suspend fun writeFile(text: String){
        withContext(Dispatchers.IO){
            getFile().appendText(text)
        }
    }

    fun getFile() = File(App.appContext.cacheDir, Constants.TERMS_FILE_NAME)

}