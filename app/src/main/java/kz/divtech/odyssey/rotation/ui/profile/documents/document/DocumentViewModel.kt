package kz.divtech.odyssey.rotation.ui.profile.documents.document

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.launch
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.profile.Document
import kz.divtech.odyssey.shared.domain.repository.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class DocumentViewModel @Inject constructor(val profileRepository: ProfileRepository) : ViewModel() {
    val pBarVisibility = ObservableInt(View.GONE)

    private val _updateDocumentResult = MutableLiveData<Resource<HttpResponse>>()
    val updateDocumentResult: LiveData<Resource<HttpResponse>> = _updateDocumentResult
    fun updateDocument(document: Document){
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            val response = profileRepository.updateDocument(document)
            _updateDocumentResult.value = response
            pBarVisibility.set(View.GONE)
        }
    }

}