package kz.divtech.odyssey.rotation.ui.profile.documents.documents

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.domain.model.profile.documents.Documents
import kz.divtech.odyssey.rotation.domain.repository.EmployeeRepository
import timber.log.Timber

class DocumentsViewModel(employeeRepository: EmployeeRepository) : ViewModel() {

    private val _documents: MutableLiveData<Documents> = MutableLiveData()
    val documents : LiveData<Documents> = _documents

    val pBarVisibility = ObservableInt(View.GONE)

    fun getAllDocuments(){
        pBarVisibility.set(View.VISIBLE)
        viewModelScope.launch {
            try{
                val response = RetrofitClient.getApiService().getDocuments()
                if(response.isSuccessful){
                    _documents.postValue(response.body())
                }
            }catch (e: Exception){
                Timber.e("exception - ${e.message}")
            }
            pBarVisibility.set(View.GONE)
        }
    }

    val employeeLiveData: LiveData<Employee> = employeeRepository.employee.asLiveData()

    class DocumentsViewModelFactory(private val repository: EmployeeRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(DocumentsViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return DocumentsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown viewModel class")
        }
    }

}