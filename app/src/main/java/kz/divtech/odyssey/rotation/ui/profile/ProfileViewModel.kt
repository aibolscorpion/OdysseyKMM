package kz.divtech.odyssey.rotation.ui.profile

import androidx.lifecycle.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.domain.repository.*
import kz.divtech.odyssey.rotation.utils.SharedPrefs

class ProfileViewModel(
    private val tripsRepository: TripsRepository,
    private val employeeRepository: EmployeeRepository,
    private val faqRepository: FaqRepository,
    private val documentRepository: DocumentRepository,
    private val newsRepository: NewsRepository,
    private val articleRepository: ArticleRepository,
    private val notificationRepository: NotificationRepository): ViewModel() {

    private val _isSuccessfullyLoggedOut = MutableLiveData<Boolean>()
    val isSuccessfullyLoggedOut = _isSuccessfullyLoggedOut

    fun logout(){
        viewModelScope.launch {
            employeeRepository.logoutFromServer()
            _isSuccessfullyLoggedOut.postValue(true)
        }
    }

    class ProfileViewModelFactory(
        private val tripsRepository: TripsRepository,
        private val employeeRepository: EmployeeRepository,
        private val faqRepository: FaqRepository,
        private val documentRepository: DocumentRepository,
        private val newsRepository: NewsRepository,
        private val articleRepository: ArticleRepository,
        private val notificationRepository: NotificationRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(ProfileViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return ProfileViewModel(tripsRepository, employeeRepository,
                    faqRepository, documentRepository, newsRepository,
                    articleRepository, notificationRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    val employeeLiveData: LiveData<Employee> = employeeRepository.employee.asLiveData()

    fun deleteAllDataAsync() = viewModelScope.async{
        SharedPrefs().clearAuthToken()
        val deleteTripsAsync = async { tripsRepository.deleteTrips() }
        val deleteEmployeeAsync = async { employeeRepository.deleteEmployee() }
        val deleteFaqAsync = async { faqRepository.deleteFaq() }
        val deleteDocumentsAsync = async { documentRepository.deleteDocuments() }
        val deleteNewsAsync = async { newsRepository.deleteNews() }
        val deleteFullArticlesAsync = async { articleRepository.deleteFullArticles() }
        val deleteNotificationsAsync = async { notificationRepository.deleteNotifications() }

        deleteTripsAsync.await()
        deleteEmployeeAsync.await()
        deleteFaqAsync.await()
        deleteDocumentsAsync.await()
        deleteNewsAsync.await()
        deleteFullArticlesAsync.await()
        deleteNotificationsAsync.await()
    }

}