package kz.divtech.odyssey.rotation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var navController : NavController

    @OptIn(NavigationUiSaveStateControl::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = binding.bottomNavigationView
        NavigationUI.setupWithNavController(bottomNavigationView, navController, false)
        setSupportActionBar(binding.mainToolbar)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.mainFragment,
            R.id.tripsFragment, R.id.helpFragment, R.id.profileFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.personalDataFragment, R.id.documentsFragment,
                R.id.notificationFragment, R.id.questionsAnswersFragment,
                    R.id.pressServiceFragment-> binding.mainToolbar.setNavigationIcon(R.drawable.icons_tabs_back)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}
