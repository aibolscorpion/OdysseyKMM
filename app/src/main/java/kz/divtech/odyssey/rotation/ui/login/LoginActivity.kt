package kz.divtech.odyssey.rotation.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.phone.SmsRetriever
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.ActivityLoginBinding
import kz.divtech.odyssey.rotation.utils.Utils

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataBinding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)

        SmsRetriever.getClient(this).startSmsRetriever().addOnFailureListener {  exception ->
            Utils.showErrorMessage(this,dataBinding.root,  exception.message.toString())
        }
    }

}
