package kz.divtech.odyssey.rotation.utils

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.Fragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.Config

object ContactUtil {

    fun callSupport(fragment: Fragment, supportPhoneNumber: String){
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("${Config.CALL} $supportPhoneNumber")
        fragment.startActivity(intent)
    }

    fun writeSupportOnWhatsapp(fragment: Fragment, whatsappNumber: String){
        val uri = Uri.parse("${Config.WHATSAPP}$whatsappNumber")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.`package` = Config.WHATSAPP_PACKAGE_NAME
        try{
            fragment.startActivity(intent)
        }catch (e: ActivityNotFoundException){
            Toast.makeText(fragment.requireContext(), R.string.you_dont_have_whatsapp, Toast.LENGTH_LONG).show()
        }
    }

    fun writeSupportOnTelegram(fragment: Fragment, telegramId: String){
        val uri = Uri.parse("${Config.TELEGRAM}$telegramId")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.`package` = Config.TELEGRAM_PACKAGE_NAME
        try{
            fragment.startActivity(intent)
        }catch (e: ActivityNotFoundException){
            Toast.makeText(fragment.requireContext(), R.string.you_dont_have_telegram, Toast.LENGTH_LONG).show()
        }
    }
}