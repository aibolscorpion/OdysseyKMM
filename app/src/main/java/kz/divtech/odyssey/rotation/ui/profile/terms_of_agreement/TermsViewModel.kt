package kz.divtech.odyssey.rotation.ui.profile.terms_of_agreement

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.remote.result.*
import kz.divtech.odyssey.rotation.domain.repository.TermsRepository
import java.io.File

class TermsViewModel(private val repository: TermsRepository): ViewModel(){
    private val _failureResult = MutableLiveData<Throwable>()
    val failureResult: LiveData<Throwable> = _failureResult

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text

    val progressVisibility = ObservableInt(View.GONE)
    val uaConfirmedLiveData = repository.uaConfirmed

    fun getUserAgreementFromServer(){
        progressVisibility.set(View.VISIBLE)
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getTermsOfAgreement()
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

    fun updateUAConfirm(){
        progressVisibility.set(View.VISIBLE)
        viewModelScope.launch {
            val response = repository.updateUAConfirm()
            if(response.isFailure()){
                response.asFailure().error?.let {
                    _failureResult.postValue(it)
                }
            }
            progressVisibility.set(View.GONE)
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

    class TermsViewModelFactory(private val repository: TermsRepository): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(TermsViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return TermsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown viewModel class")
        }
    }
}