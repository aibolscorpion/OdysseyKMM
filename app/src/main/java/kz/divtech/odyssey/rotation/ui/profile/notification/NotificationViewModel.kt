package kz.divtech.odyssey.rotation.ui.profile.notification

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.Notification
import kz.divtech.odyssey.rotation.domain.repository.NotificationRepository

class NotificationViewModel(private val notificationRepository: NotificationRepository): ViewModel() {

    val pBarVisibility = ObservableInt(View.GONE)

    fun getPagingNotifications(): Flow<PagingData<Notification>> {
        return notificationRepository.getPagingNotifications()
            .cachedIn(viewModelScope)
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