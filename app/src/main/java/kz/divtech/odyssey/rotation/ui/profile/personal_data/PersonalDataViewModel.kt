package kz.divtech.odyssey.rotation.ui.profile.personal_data

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.common.utils.Event
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.profile.Profile
import kz.divtech.odyssey.shared.domain.repository.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class PersonalDataViewModel @Inject constructor(val profileRepository: ProfileRepository): ViewModel() {
    val employeeLiveData: LiveData<Profile?> = profileRepository.getProfileFromDb().asLiveData()
    val pBarVisibility = ObservableInt(View.GONE)

    private var _personalDataUpdated = MutableLiveData<Event<Boolean>>()
    val personalDataUpdated: LiveData<Event<Boolean>> = _personalDataUpdated

    private val _updatePersonalResult = MutableLiveData<Resource<HttpResponse>>()
    val updatePersonalResult: LiveData<Resource<HttpResponse>> = _updatePersonalResult


    fun updatePersonalData(profile: Profile, citizenshipChanged: Boolean){
        pBarVisibility.set(View.VISIBLE)
        viewModelScope.launch {
            val response = profileRepository.updateProfile(profile)
            if(response is Resource.Success){
                profileRepository.getProfile()
                _personalDataUpdated.value = Event(citizenshipChanged)
            }
            _updatePersonalResult.value = response
            pBarVisibility.set(View.GONE)
        }
    }
}