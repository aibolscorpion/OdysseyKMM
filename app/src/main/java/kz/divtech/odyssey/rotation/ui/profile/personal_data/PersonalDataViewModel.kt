package kz.divtech.odyssey.rotation.ui.profile.personal_data

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Employee
import kz.divtech.odyssey.rotation.data.repository.ProfileRepository
import kz.divtech.odyssey.rotation.common.utils.Event
import okhttp3.ResponseBody
import kz.divtech.odyssey.rotation.data.remote.result.*
import javax.inject.Inject

@HiltViewModel
class PersonalDataViewModel @Inject constructor(val profileRepository: ProfileRepository): ViewModel() {
    val employee = profileRepository.employee
    val pBarVisibility = ObservableInt(View.GONE)

    private var _personalDataUpdated = MutableLiveData<Event<Boolean>>()
    val personalDataUpdated: LiveData<Event<Boolean>> = _personalDataUpdated

    private val _updatePersonalResult = MutableLiveData<Result<ResponseBody>>()
    val updatePersonalResult: LiveData<Result<ResponseBody>> = _updatePersonalResult


    fun updatePersonalData(employee: Employee, citizenshipChanged: Boolean){
        pBarVisibility.set(View.VISIBLE)
        viewModelScope.launch {
            val response = profileRepository.updateProfile(employee)
            if(response.isSuccess()){
                profileRepository.getAndInsertProfile()
                _personalDataUpdated.value = Event(citizenshipChanged)
            }
            _updatePersonalResult.value = response
            pBarVisibility.set(View.GONE)
        }
    }
}