package kz.divtech.odyssey.rotation.ui.profile

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.domain.repository.EmployeeRepository
import kz.divtech.odyssey.rotation.domain.repository.TripsRepository
import kz.divtech.odyssey.rotation.utils.SharedPrefs

class ProfileViewModel(
    private val tripsRepository: TripsRepository,
    private val employeeRepository: EmployeeRepository): ViewModel() {
    private val _isSuccessfullyLoggedOut = MutableLiveData<Boolean>()
    val isSuccessfullyLoggedOut = _isSuccessfullyLoggedOut

    fun logout(){
        viewModelScope.launch {
            val callResult = RetrofitClient.getApiService().logout()
            _isSuccessfullyLoggedOut.postValue(callResult.isSuccessful)
        }
    }

    class ProfileViewModelFactory(val tripsRepository: TripsRepository,
                                  private val employeeRepository: EmployeeRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(ProfileViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return ProfileViewModel(tripsRepository, employeeRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    val employeeLiveData: LiveData<Employee> = employeeRepository.employee.asLiveData()

    fun deleteDataFromDB() = viewModelScope.launch{
            SharedPrefs().clearAuthToken()
            tripsRepository.deleteTrips()
            employeeRepository.deleteEmployee()
        }

}