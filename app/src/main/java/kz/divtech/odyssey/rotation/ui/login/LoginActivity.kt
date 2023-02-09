package kz.divtech.odyssey.rotation.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.auth.api.phone.SmsRetriever
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.data.local.AppDatabase
import kz.divtech.odyssey.rotation.databinding.ActivityLoginBinding
import kz.divtech.odyssey.rotation.domain.repository.EmployeeRepository
import kz.divtech.odyssey.rotation.utils.InputUtils.showErrorMessage
import kz.divtech.odyssey.rotation.utils.SharedPrefs

class LoginActivity : AppCompatActivity(){
    private val database by lazy { AppDatabase.getDatabase(this) }
    val employeeRepository by lazy { EmployeeRepository(database.dao()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataBinding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)

        if(SharedPrefs.isLoggedIn(this)) {
            openMainActivity()
        }

        SmsRetriever.getClient(this).startSmsRetriever().addOnFailureListener {  exception ->
            showErrorMessage(this,dataBinding.root,  exception.message.toString())
        }

    }

    private fun openMainActivity() {
        val navController = (supportFragmentManager.findFragmentById(R.id.loginNavHostFragment)
                as NavHostFragment).navController
        navController.navigate(R.id.action_global_mainActivity, intent.extras)
    }

}
