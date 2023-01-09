package kz.divtech.odyssey.rotation.ui.profile.terms_of_agreement

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import timber.log.Timber

class TermsOfAgreementViewModel: ViewModel(){
    private val _htmlMutableLiveData = MutableLiveData<String>()
    val htmlLiveData: LiveData<String> = _htmlMutableLiveData

    val progressVisibility = ObservableInt(View.GONE)

    fun getUserAgreement(){
        progressVisibility.set(View.VISIBLE)
        viewModelScope.launch {
            try{
                val response = RetrofitClient.getApiService().getUserAgreement()
                if(response.isSuccessful){
                    val value = response.body()?.string()
                    _htmlMutableLiveData.postValue(value!!)
                }else{
                    progressVisibility.set(View.GONE)
                }
            }catch (e: Exception){
                Timber.e("exception - ${e.message}")
                progressVisibility.set(View.GONE)
            }
        }
    }

}