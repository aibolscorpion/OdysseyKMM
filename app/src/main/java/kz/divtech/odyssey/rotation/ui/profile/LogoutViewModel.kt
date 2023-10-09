package kz.divtech.odyssey.rotation.ui.profile

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.common.App
import kz.divtech.odyssey.rotation.data.repository.ArticleRepository
import kz.divtech.odyssey.rotation.data.repository.EmployeeRepository
import kz.divtech.odyssey.rotation.data.repository.FaqRepository
import kz.divtech.odyssey.rotation.data.repository.NewsRepository
import kz.divtech.odyssey.rotation.data.repository.NotificationRepository
import kz.divtech.odyssey.rotation.data.repository.OrgInfoRepository
import kz.divtech.odyssey.rotation.data.repository.TermsRepository
import kz.divtech.odyssey.rotation.data.repository.TripsRepository
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Employee
import kz.divtech.odyssey.rotation.common.utils.SharedPrefs.clearAuthToken
import kz.divtech.odyssey.rotation.common.utils.SharedPrefs.clearUrl

class LogoutViewModel(
    private val tripsRepository: TripsRepository,
    private val employeeRepository: EmployeeRepository,
    private val faqRepository: FaqRepository,
    private val newsRepository: NewsRepository,
    private val articleRepository: ArticleRepository,
    private val notificationRepository: NotificationRepository,
    private val orgInfoRepository: OrgInfoRepository,
    private val termsRepository: TermsRepository
): ViewModel() {

    val employeeLiveData: LiveData<Employee> = employeeRepository.employee
    val uaConfirmedLiveData: LiveData<Boolean> = employeeRepository.uaConfirmed

    private val _isSuccessfullyLoggedOut = MutableLiveData<Boolean>()
    val isSuccessfullyLoggedOut = _isSuccessfullyLoggedOut

    val pBarVisibility = ObservableInt(View.GONE)

    fun getAndInsterEmployee(){
        viewModelScope.launch {
            employeeRepository.getAndInstertEmployee()
        }
    }
    fun getNotificationsFromServer() =
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            notificationRepository.getNotificationFromFirstPage()
            pBarVisibility.set(View.GONE)
        }

    fun logoutFromServer(){
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            employeeRepository.logoutFromServer()
            _isSuccessfullyLoggedOut.value = true
            pBarVisibility.set(View.GONE)
        }
    }

    fun deleteAllDataAsync() = viewModelScope.async{
        App.appContext.clearAuthToken()
        App.appContext.clearUrl()
        val deleteTripsAsync = async { tripsRepository.deleteAllTrips() }
        val deleteNearestTripAsync = async { tripsRepository.deleteNearestActiveTrip() }
        val deleteEmployeeAsync = async { employeeRepository.deleteEmployee() }
        val deleteFaqAsync = async { faqRepository.deleteFaq() }
        val deleteNewsAsync = async { newsRepository.deleteNews() }
        val deleteFullArticlesAsync = async { articleRepository.deleteFullArticles() }
        val deleteNotificationsAsync = async { notificationRepository.deleteNotifications() }
        val deleteOrgInfo = async { orgInfoRepository.deleteOrgInfo() }
        val deleteTermsFile = async { termsRepository.deleteTermsFile() }
        deleteTripsAsync.await()
        deleteNearestTripAsync.await()
        deleteEmployeeAsync.await()
        deleteFaqAsync.await()
        deleteNewsAsync.await()
        deleteFullArticlesAsync.await()
        deleteNotificationsAsync.await()
        deleteOrgInfo.await()
        deleteTermsFile.await()
    }

    class LogoutViewModelFactory(
        private val tripsRepository: TripsRepository,
        private val employeeRepository: EmployeeRepository,
        private val faqRepository: FaqRepository,
        private val newsRepository: NewsRepository,
        private val articleRepository: ArticleRepository,
        private val notificationRepository: NotificationRepository,
        private val orgInfoRepository: OrgInfoRepository,
        private val termsRepository: TermsRepository
    ) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(LogoutViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return LogoutViewModel(tripsRepository, employeeRepository,
                    faqRepository, newsRepository,
                    articleRepository, notificationRepository, orgInfoRepository, termsRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}