package kz.divtech.odyssey.rotation.ui.profile

import androidx.lifecycle.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
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
            val callResult = RetrofitClient.getApiService().logout()
            _isSuccessfullyLoggedOut.postValue(callResult.isSuccessful)
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

    private fun deleteTripsAsync() = viewModelScope.async {
        tripsRepository.deleteTrips()
    }

    private fun deleteEmployeeAsync() = viewModelScope.async {
        employeeRepository.deleteEmployee()
    }

    private fun deleteFaqAsync() = viewModelScope.async {
        faqRepository.deleteFaq()
    }

    private fun deleteDocumentsAsync() = viewModelScope.async {
        documentRepository.deleteDocuments()
    }

    private fun deleteNewsAsync() = viewModelScope.async {
        newsRepository.deleteNews()
    }

    private fun deleteFullArticlesAsync() = viewModelScope.async {
        articleRepository.deleteFullArticles()
    }

    private fun deleteNotificationsAsync() = viewModelScope.async {
        notificationRepository.deleteNotifications()
    }

    fun deleteAllDataAsync() = viewModelScope.async{
        SharedPrefs().clearAuthToken()
        val deleteTripsAsync = deleteTripsAsync()
        val deleteEmployeeAsync = deleteEmployeeAsync()
        val deleteFaqAsync = deleteFaqAsync()
        val deleteDocumentsAsync = deleteDocumentsAsync()
        val deleteNewsAsync = deleteNewsAsync()
        val deleteFullArticlesAsync = deleteFullArticlesAsync()
        val deleteNotificationsAsync = deleteNotificationsAsync()

        deleteTripsAsync.await()
        deleteEmployeeAsync.await()
        deleteFaqAsync.await()
        deleteDocumentsAsync.await()
        deleteNewsAsync.await()
        deleteFullArticlesAsync.await()
        deleteNotificationsAsync.await()
    }

}