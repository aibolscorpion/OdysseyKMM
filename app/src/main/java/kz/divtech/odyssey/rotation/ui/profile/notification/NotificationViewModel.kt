package kz.divtech.odyssey.rotation.ui.profile.notification

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.domain.repository.NotificationRepository

class NotificationViewModel(private val notificationRepository: NotificationRepository): ViewModel() {
    val notificationsLiveData = notificationRepository.notifications.asLiveData()

    val pBarVisibility = ObservableInt(View.GONE)
    val isRefreshing = ObservableBoolean()


    fun getNotificationsFromServer(){
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            notificationRepository.getNotificationsFromServer()
            pBarVisibility.set(View.GONE)
        }
    }


    fun refreshNotifications(){
        viewModelScope.launch {
            isRefreshing.set(true)
            notificationRepository.getNotificationsFromServer()
            isRefreshing.set(false)
        }
    }

    class NotificationViewModelFactory(private val notificationRepository: NotificationRepository): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(NotificationViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return NotificationViewModel(notificationRepository) as T
            }
            throw IllegalArgumentException("Unknown viewModel class")
        }
    }
}