package kz.divtech.odyssey.rotation.common.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.common.Config

object ContactUtil {

    fun callSupport(context: Context, supportPhoneNumber: String){
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("${Config.CALL} $supportPhoneNumber")
        context.startActivity(intent)
    }

    fun writeSupportOnWhatsapp(context: Context, whatsappNumber: String){
        val uri = Uri.parse("${Config.WHATSAPP}$whatsappNumber")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.`package` = Config.WHATSAPP_PACKAGE_NAME
        try{
            context.startActivity(intent)
        }catch (e: ActivityNotFoundException){
            Toast.makeText(context, R.string.you_dont_have_whatsapp, Toast.LENGTH_LONG).show()
        }
    }

    fun writeSupportOnTelegram(context: Context, telegramId: String){
        val uri = Uri.parse("${Config.TELEGRAM}$telegramId")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.`package` = Config.TELEGRAM_PACKAGE_NAME
        try{
            context.startActivity(intent)
        }catch (e: ActivityNotFoundException){
            Toast.makeText(context, R.string.you_dont_have_telegram, Toast.LENGTH_LONG).show()
        }
    }
}