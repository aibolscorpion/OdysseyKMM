package kz.divtech.odyssey.rotation.ui.profile.documents.documents

import androidx.lifecycle.*
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Employee
import kz.divtech.odyssey.rotation.domain.repository.EmployeeRepository

class DocumentsViewModel(employeeRepository: EmployeeRepository) : ViewModel() {
    val employeeLiveData: LiveData<Employee> = employeeRepository.employee.asLiveData()

    class DocumentsViewModelFactory(private val employeeRepository: EmployeeRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(DocumentsViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return DocumentsViewModel(employeeRepository) as T
            }
            throw IllegalArgumentException("Unknown viewModel class")
        }
    }

}