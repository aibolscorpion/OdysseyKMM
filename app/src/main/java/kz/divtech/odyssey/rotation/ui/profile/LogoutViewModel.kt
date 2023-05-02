package kz.divtech.odyssey.rotation.ui.profile

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Employee
import kz.divtech.odyssey.rotation.domain.repository.*
import kz.divtech.odyssey.rotation.utils.SharedPrefs

class LogoutViewModel(
    private val tripsRepository: TripsRepository,
    private val employeeRepository: EmployeeRepository,
    private val faqRepository: FaqRepository,
    private val newsRepository: NewsRepository,
    private val articleRepository: ArticleRepository,
    private val notificationRepository: NotificationRepository,
    private val orgInfoRepository: OrgInfoRepository): ViewModel() {

    private val _isSuccessfullyLoggedOut = MutableLiveData<Boolean>()
    val isSuccessfullyLoggedOut = _isSuccessfullyLoggedOut

    val pBarVisibility = ObservableInt(View.GONE)

    fun getNotificationsFromServer() =
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            notificationRepository.getNotificationFromFirstPage(true)
            pBarVisibility.set(View.GONE)
        }

    fun logoutFromServer(){
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            employeeRepository.logoutFromServer()
            _isSuccessfullyLoggedOut.postValue(true)
            pBarVisibility.set(View.GONE)
        }
    }

    class LogoutViewModelFactory(
        private val tripsRepository: TripsRepository,
        private val employeeRepository: EmployeeRepository,
        private val faqRepository: FaqRepository,
        private val newsRepository: NewsRepository,
        private val articleRepository: ArticleRepository,
        private val notificationRepository: NotificationRepository,
        private val orgInfoRepository: OrgInfoRepository
    ) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(LogoutViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return LogoutViewModel(tripsRepository, employeeRepository,
                    faqRepository, newsRepository,
                    articleRepository, notificationRepository, orgInfoRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    val employeeLiveData: LiveData<Employee> = employeeRepository.employee.asLiveData()

    fun deleteAllDataAsync() = viewModelScope.async{
        SharedPrefs.clearAuthToken(App.appContext)
        val deleteTripsAsync = async { tripsRepository.deleteAllTrips() }
        val deleteEmployeeAsync = async { employeeRepository.deleteEmployee() }
        val deleteFaqAsync = async { faqRepository.deleteFaq() }
        val deleteNewsAsync = async { newsRepository.deleteNews() }
        val deleteFullArticlesAsync = async { articleRepository.deleteFullArticles() }
        val deleteNotificationsAsync = async { notificationRepository.deleteNotifications() }
        val deleteOrgInfo = async { orgInfoRepository.deleteOrgInfo() }
        deleteTripsAsync.await()
        deleteEmployeeAsync.await()
        deleteFaqAsync.await()
        deleteNewsAsync.await()
        deleteFullArticlesAsync.await()
        deleteNotificationsAsync.await()
        deleteOrgInfo.await()
    }

}