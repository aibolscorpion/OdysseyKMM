package kz.divtech.odyssey.rotation.ui.login.update_phone

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.launch
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.UpdatePhoneRequest
import kz.divtech.odyssey.shared.domain.repository.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class UpdatePhoneViewModel @Inject constructor(private val profileRepository: ProfileRepository) : ViewModel() {
    private val _updatePhoneResult = MutableLiveData<Resource<HttpResponse>>()
    val updatePhoneResult: LiveData<Resource<HttpResponse>> = _updatePhoneResult

    val pBarVisibility = ObservableInt(View.GONE)

    fun updatePhoneNumber(request: UpdatePhoneRequest) {
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            val response = profileRepository.updatePhoneNumberWihoutAuth(request)
            _updatePhoneResult.value = response
            pBarVisibility.set(View.GONE)
        }
    }

}