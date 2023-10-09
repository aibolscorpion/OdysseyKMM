package kz.divtech.odyssey.rotation.common

import com.google.android.play.core.install.model.AppUpdateType

object Config {
    const val DATABASE_NAME = "odyssey_rotation"
    const val COUNT_DOWN_TIMER_SECONDS = 60
    const val COUNT_DOWN_INTERVAL = 1000L
    const val PROXY_HOST = "https://proxy.odyssey.kz"
    const val API = "/api/"
    const val IS_TEST = true
    const val UPDATE_TYPE = AppUpdateType.IMMEDIATE
    const val COUNTRY_CODE = "7"
    const val DEVICE_ID_KEY = "deviceId"
    const val AUTHORIZATION_KEY = "Authorization"
    const val AUTHORIZATION_VALUE_PREFIX = "Bearer"
    const val IIN_LENGTH = 12
    const val CALL = "tel:"
    const val WHATSAPP_PACKAGE_NAME = "com.whatsapp"
    const val WHATSAPP = "https://wa.me/"
    const val TELEGRAM_PACKAGE_NAME ="org.telegram.messenger"
    const val TELEGRAM = "https://telegram.me/"
}