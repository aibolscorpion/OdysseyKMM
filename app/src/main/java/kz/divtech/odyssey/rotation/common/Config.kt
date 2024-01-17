package kz.divtech.odyssey.rotation.common

import com.google.android.play.core.install.model.AppUpdateType
import java.io.File

object Config {
    const val COUNT_DOWN_TIMER_SECONDS = 60
    const val COUNT_DOWN_INTERVAL = 1000L
    const val API = "/api/"
    const val UPDATE_TYPE = AppUpdateType.IMMEDIATE
    const val COUNTRY_CODE = "7"
    const val IIN_LENGTH = 12
    const val CALL = "tel:"
    const val WHATSAPP_PACKAGE_NAME = "com.whatsapp"
    const val WHATSAPP = "https://wa.me/"
    const val TELEGRAM_PACKAGE_NAME ="org.telegram.messenger"
    const val TELEGRAM = "https://telegram.me/"
    val termsOfAgreementFile: File = File(App.appContext.cacheDir, Constants.TERMS_FILE_NAME)
}