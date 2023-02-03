package kz.divtech.odyssey.rotation.ui.pushnotification

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kz.divtech.odyssey.rotation.utils.SharedPrefs
import timber.log.Timber

class PushNotificationService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {

        Timber.i("title = ${message.notification?.title}")
        Timber.i("body = ${message.notification?.body}")

        super.onMessageReceived(message)

    }

    override fun onNewToken(token: String) {
        SharedPrefs.saveFirebaseToken(token, this)
        super.onNewToken(token)
    }

}