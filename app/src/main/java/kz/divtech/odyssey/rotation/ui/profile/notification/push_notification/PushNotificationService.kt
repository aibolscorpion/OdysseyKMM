package kz.divtech.odyssey.rotation.ui.profile.notification.push_notification

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.divtech.odyssey.shared.data.local.data_store.DataStoreManager
import javax.inject.Inject


@AndroidEntryPoint
class PushNotificationService : FirebaseMessagingService() {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    @Inject
    lateinit var dataStoreManager: DataStoreManager
    override fun onMessageReceived(message: RemoteMessage) {
        val intent = message.toIntent().setClassName(applicationContext.packageName,
            "${applicationContext.packageName}.ui.MainActivity")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

        super.onMessageReceived(message)

    }

    override fun onNewToken(token: String) {
        coroutineScope.launch {
            dataStoreManager.saveFirebaseToken(token)
        }
        super.onNewToken(token)
    }

}