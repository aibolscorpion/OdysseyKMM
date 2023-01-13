package kz.divtech.odyssey.rotation.ui.profile.documents.documents

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.domain.repository.DocumentRepository
import kz.divtech.odyssey.rotation.domain.repository.EmployeeRepository

class DocumentsViewModel(employeeRepository: EmployeeRepository,
                         private val documentRepository: DocumentRepository) : ViewModel() {

    val documentsLiveData = documentRepository.documents.asLiveData()

    val pBarVisibility = ObservableInt(View.GONE)

    fun getDocumentsFromServer(){
        pBarVisibility.set(View.VISIBLE)
        viewModelScope.launch {
            documentRepository.getDocumentsFromServer()
            pBarVisibility.set(View.GONE)
        }
    }

    val employeeLiveData: LiveData<Employee> = employeeRepository.employee.asLiveData()

    class DocumentsViewModelFactory(private val employeeRepository: EmployeeRepository,
                private val documentRepository: DocumentRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(DocumentsViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return DocumentsViewModel(employeeRepository, documentRepository) as T
            }
            throw IllegalArgumentException("Unknown viewModel class")
        }
    }

}