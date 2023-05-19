package kz.divtech.odyssey.rotation.ui.profile.documents.document

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Document
import kz.divtech.odyssey.rotation.domain.repository.EmployeeRepository

class DocumentViewModel(val employeeRepository: EmployeeRepository) : ViewModel() {
    val pBarVisibility = ObservableInt(View.GONE)
    private var _documentUpdated = MutableLiveData<Boolean>()
    val documentUpdated: LiveData<Boolean> get() = _documentUpdated
    fun updateDocument(document: Document){
        pBarVisibility.set(View.VISIBLE)
        viewModelScope.launch {
            val response = RetrofitClient.getApiService().updateDocument(document)
            if(response.isSuccess()){
                employeeRepository.getEmployeeFromServer()
                _documentUpdated.value = true
            }
            pBarVisibility.set(View.GONE)
        }

    }

    class DocumentViewModelFactory(private val employeeRepository: EmployeeRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(DocumentViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return DocumentViewModel(employeeRepository) as T
            }
            throw IllegalArgumentException("Unknown viewModel class")
        }
    }
}