package kz.divtech.odyssey.rotation.ui.profile.documents.documents

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.divtech.odyssey.shared.domain.model.profile.Profile
import kz.divtech.odyssey.shared.domain.repository.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class DocumentsViewModel @Inject constructor(profileRepository: ProfileRepository) : ViewModel() {
    val employeeLiveData: LiveData<Profile?> = profileRepository.getProfileFromDb().asLiveData()

}