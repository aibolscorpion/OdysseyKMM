package kz.divtech.odyssey.rotation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.jakewharton.threetenabp.AndroidThreeTen
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        AndroidThreeTen.init(application)


        val bottomNavigationView = binding.bottomNavigationView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)

        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.icons_tabs_back)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.mainNavHostFragment)
        navController.popBackStack()

        return super.onSupportNavigateUp()
    }

}
