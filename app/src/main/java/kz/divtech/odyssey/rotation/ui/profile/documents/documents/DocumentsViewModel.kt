package kz.divtech.odyssey.rotation.ui.profile.documents.documents

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Employee
import kz.divtech.odyssey.rotation.data.repository.EmployeeRepository
import javax.inject.Inject

@HiltViewModel
class DocumentsViewModel @Inject constructor(employeeRepository: EmployeeRepository) : ViewModel() {
    val employeeLiveData: LiveData<Employee> = employeeRepository.employee

}