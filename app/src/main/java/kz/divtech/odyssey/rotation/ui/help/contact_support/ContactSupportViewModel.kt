package kz.divtech.odyssey.rotation.ui.help.contact_support

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import kz.divtech.odyssey.rotation.domain.repository.*

class ContactSupportViewModel(orgInfoRepository: OrgInfoRepository) : ViewModel(){

    val orgInfoLiveData = orgInfoRepository.orgInfo.asLiveData()

    class ContactSupportViewModelFactory(
        private val orgInfoRepository: OrgInfoRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(ContactSupportViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return ContactSupportViewModel(orgInfoRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}