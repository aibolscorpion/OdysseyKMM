package kz.divtech.odyssey.rotation.ui.profile

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.common.Config
import kz.divtech.odyssey.shared.data.local.data_store.DataStoreManager
import kz.divtech.odyssey.shared.domain.model.profile.Profile
import kz.divtech.odyssey.shared.domain.repository.ArticleRepository
import kz.divtech.odyssey.shared.domain.repository.FaqRepository
import kz.divtech.odyssey.shared.domain.repository.LoginRepository
import kz.divtech.odyssey.shared.domain.repository.NewsRepository
import kz.divtech.odyssey.shared.domain.repository.NotificationsRepository
import kz.divtech.odyssey.shared.domain.repository.OrgInfoRepository
import kz.divtech.odyssey.shared.domain.repository.ProfileRepository
import kz.divtech.odyssey.shared.domain.repository.TermsRepository
import kz.divtech.odyssey.shared.domain.repository.TripsRepository
import javax.inject.Inject

@HiltViewModel
class LogoutViewModel @Inject constructor(
    private val tripsRepository: TripsRepository,
    private val profileRepository: ProfileRepository,
    private val faqRepository: FaqRepository,
    private val newsRepository: NewsRepository,
    private val articleRepository: ArticleRepository,
    private val notificationRepository: NotificationsRepository,
    private val orgInfoRepository: OrgInfoRepository,
    private val termsRepository: TermsRepository,
    private val loginRepository: LoginRepository,
    private val dataStoreManager: DataStoreManager
): ViewModel() {

    val employeeLiveData: LiveData<Profile?> = profileRepository.getProfileFromDb().asLiveData()
    val uaConfirmedLiveData: LiveData<Long?> = termsRepository.getUaConfirmedFromDB().asLiveData()

    private val _isSuccessfullyLoggedOut = MutableLiveData<Boolean>()
    val isSuccessfullyLoggedOut = _isSuccessfullyLoggedOut

    val pBarVisibility = ObservableInt(View.GONE)

    fun getProfileFromServer(){
        viewModelScope.launch {
            profileRepository.getProfile()
        }
    }
    fun getNotificationsFromServer() =
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            notificationRepository.getNotificationsFirstPage()
            pBarVisibility.set(View.GONE)
        }

    fun logoutFromServer(){
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            loginRepository.logout()
            _isSuccessfullyLoggedOut.value = true
            pBarVisibility.set(View.GONE)
        }
    }

    fun deleteAllDataAsync() = viewModelScope.async{
        val deleteUrl = async { dataStoreManager.clearUrl() }
        val deleteAuthToken = async { dataStoreManager.clearAuthToken() }
        val deleteTripsAsync = async { tripsRepository.deleteAllTrips() }
        val deleteEmployeeAsync = async { profileRepository.deleteProfile() }
        val deleteFaqAsync = async { faqRepository.deleteFaq() }
        val deleteNewsAsync = async { newsRepository.deleteNews() }
        val deleteFullArticlesAsync = async { articleRepository.deleteFullArticles() }
        val deleteNotificationsAsync = async { notificationRepository.deleteNoficiations() }
        val deleteOrgInfo = async { orgInfoRepository.deleteOrgInfo() }
        val deleteTermsFile = async { termsRepository.deleteTermsFile(Config.termsOfAgreementFile) }
        deleteUrl.await()
        deleteAuthToken.await()
        deleteTripsAsync.await()
        deleteEmployeeAsync.await()
        deleteFaqAsync.await()
        deleteNewsAsync.await()
        deleteFullArticlesAsync.await()
        deleteNotificationsAsync.await()
        deleteOrgInfo.await()
        deleteTermsFile.await()
    }


}