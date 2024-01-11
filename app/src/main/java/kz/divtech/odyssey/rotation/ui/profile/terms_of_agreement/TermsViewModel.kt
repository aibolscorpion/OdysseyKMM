package kz.divtech.odyssey.rotation.ui.profile.terms_of_agreement

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.divtech.odyssey.rotation.common.Config
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.repository.TermsRepository
import javax.inject.Inject

@HiltViewModel
class TermsViewModel @Inject constructor(private val repository: TermsRepository): ViewModel(){
    private val _failureResult = MutableLiveData<String>()
    val failureResult: LiveData<String> = _failureResult

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text

    val progressVisibility = ObservableInt(View.GONE)
     fun getUaConfirmedFromDB() = repository.getUaConfirmedFromDB().asLiveData()

    fun getUserAgreementFromServer(){
        progressVisibility.set(View.VISIBLE)
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getTermsOfAgreement()
            if(response is Resource.Success){
                response.data?.let {
                    writeFile(it)
                    readFile()
                }
            }else{
                _failureResult.postValue(response.message.toString())
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
            if(response is Resource.Error){
                _failureResult.postValue(response.message.toString())
            }
            progressVisibility.set(View.GONE)
        }
    }

    suspend fun readFile(){
        withContext(Dispatchers.IO){
            _text.postValue(Config.termsOfAgreementFile.readText())
        }
    }

    private suspend fun writeFile(text: String){
        withContext(Dispatchers.IO){
            Config.termsOfAgreementFile.appendText(text)
        }
    }

}