package kz.divtech.odyssey.rotation.ui.profile.documents.document

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Document
import kz.divtech.odyssey.rotation.data.repository.ProfileRepository
import okhttp3.ResponseBody
import kz.divtech.odyssey.rotation.data.remote.result.*
import kz.divtech.odyssey.rotation.data.remote.retrofit.ApiService
import javax.inject.Inject

@HiltViewModel
class DocumentViewModel @Inject constructor(val profileRepository: ProfileRepository,
                                            private val baseService: ApiService) : ViewModel() {
    val pBarVisibility = ObservableInt(View.GONE)
    private var _documentUpdated = MutableLiveData<Boolean>()
    val documentUpdated: LiveData<Boolean> get() = _documentUpdated

    private val _updateDocumentResult = MutableLiveData<Result<ResponseBody>>()
    val updateDocumentResult: LiveData<Result<ResponseBody>> = _updateDocumentResult
    fun updateDocument(document: Document){
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            val response = baseService.updateDocument(document)
            if(response.isSuccess()){
                profileRepository.getAndInsertProfile()
                _documentUpdated.value = true
            }
            _updateDocumentResult.value = response
            pBarVisibility.set(View.GONE)
        }
    }

}