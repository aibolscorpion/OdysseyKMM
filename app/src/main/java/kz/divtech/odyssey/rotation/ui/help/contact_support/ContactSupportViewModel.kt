package kz.divtech.odyssey.rotation.ui.help.contact_support

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.divtech.odyssey.rotation.data.repository.OrgInfoRepository
import javax.inject.Inject

@HiltViewModel
class ContactSupportViewModel @Inject constructor(orgInfoRepository: OrgInfoRepository) : ViewModel(){

    val orgInfoLiveData = orgInfoRepository.orgInfo.asLiveData()

}