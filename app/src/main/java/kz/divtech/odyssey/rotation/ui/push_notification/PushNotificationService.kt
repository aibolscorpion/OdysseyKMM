package kz.divtech.odyssey.rotation.ui.push_notification

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kz.divtech.odyssey.rotation.utils.SharedPrefs


class PushNotificationService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        val intent = message.toIntent().setClassName(applicationContext.packageName,
            "${applicationContext.packageName}.ui.login.LoginActivity")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

        super.onMessageReceived(message)

    }

    override fun onNewToken(token: String) {
        SharedPrefs.saveFirebaseToken(token, this)
        super.onNewToken(token)
    }

}