package kz.divtech.odyssey.rotation.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import kz.divtech.odyssey.rotation.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.loginNavHostFragment)
        navController.popBackStack()

        return super.onSupportNavigateUp()
    }



}
