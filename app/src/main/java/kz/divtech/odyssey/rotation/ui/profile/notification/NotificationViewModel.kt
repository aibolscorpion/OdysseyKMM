package kz.divtech.odyssey.rotation.ui.profile.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.Notification
import kz.divtech.odyssey.rotation.data.repository.NotificationRepository
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val notificationRepository: NotificationRepository): ViewModel() {

    fun getPagingNotifications(): Flow<PagingData<Notification>> {
        return notificationRepository.getPagingNotifications()
            .cachedIn(viewModelScope)
    }

}