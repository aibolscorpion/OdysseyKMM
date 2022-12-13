package kz.divtech.odyssey.rotation.ui.help.contact_support

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.Fragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.Config

object ContactSupport {

    fun callSupport(fragment: Fragment){
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("${Config.CALL} ${Config.SUPPORT_PHONE_NUMBER}")
        fragment.startActivity(intent)
    }

    fun writeSupportOnWhatsapp(fragment: Fragment){
        val uri = Uri.parse("${Config.WHATSAPP}${Config.SUPPORT_WHATSAPP_NUMBER}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.`package` = Config.WHATSAPP_PACKAGE_NAME
        try{
            fragment.startActivity(intent)
        }catch (e: ActivityNotFoundException){
            Toast.makeText(fragment.requireContext(), R.string.you_dont_have_whatsapp, Toast.LENGTH_LONG).show()
        }
    }

    fun writeSupportOnTelegram(fragment: Fragment){
        val uri = Uri.parse("${Config.TELEGRAM}${Config.SUPPORT_TELEGRAM_ID}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.`package` = Config.TELEGRAM_PACKAGE_NAME
        try{
            fragment.startActivity(intent)
        }catch (e: ActivityNotFoundException){
            Toast.makeText(fragment.requireContext(), R.string.you_dont_have_telegram, Toast.LENGTH_LONG).show()
        }
    }
}