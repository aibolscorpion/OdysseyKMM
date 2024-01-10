package kz.divtech.odyssey.rotation.ui.help.contact_support

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.divtech.odyssey.shared.domain.model.OrgInfo
import kz.divtech.odyssey.shared.domain.repository.OrgInfoRepository
import javax.inject.Inject

@HiltViewModel
class ContactSupportViewModel @Inject constructor(
    private val orgInfoRepository: OrgInfoRepository) : ViewModel(){
    suspend fun getOrgInfoFromDB(): LiveData<OrgInfo?> = orgInfoRepository.getOrgInfoFromDB().asLiveData()

}