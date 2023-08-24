package kz.divtech.odyssey.rotation.ui.profile.notification.push_notification

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kz.divtech.odyssey.rotation.utils.SharedPrefs.saveFirebaseToken


class PushNotificationService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        val intent = message.toIntent().setClassName(applicationContext.packageName,
            "${applicationContext.packageName}.ui.MainActivity")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

        super.onMessageReceived(message)

    }

    override fun onNewToken(token: String) {
        saveFirebaseToken(token)
        super.onNewToken(token)
    }

}