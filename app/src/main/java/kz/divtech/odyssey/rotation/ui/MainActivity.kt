package kz.divtech.odyssey.rotation.ui

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.data.local.AppDatabase
import kz.divtech.odyssey.rotation.databinding.ActivityMainBinding
import kz.divtech.odyssey.rotation.domain.repository.*
import kz.divtech.odyssey.rotation.ui.push_notification.NotificationListener
import kz.divtech.odyssey.rotation.ui.push_notification.PermissionRationale


class MainActivity : AppCompatActivity(), NotificationListener {

    private lateinit var navController : NavController
    private val database by lazy { AppDatabase.getDatabase(this) }
    val tripsRepository by lazy { TripsRepository(database.dao()) }
    val employeeRepository by lazy { EmployeeRepository(database.dao()) }
    val faqRepository by lazy { FaqRepository(database.dao()) }
    val documentRepository by lazy { DocumentRepository(database.dao()) }
    val newsRepository by lazy { NewsRepository(database.dao()) }
    val articleRepository by lazy { ArticleRepository(database.dao()) }
    val notificationRepository by lazy { NotificationRepository(database.dao()) }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) {}

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
                R.id.notificationFragment, R.id.faqFragment,
                    R.id.newsFragment-> binding.mainToolbar.setNavigationIcon(R.drawable.icons_tabs_back)
            }
        }
        checkPermission()



//        Timber.i("title = ${intent.extras?.getString("title")}")
//        Timber.i("id = ${intent.extras?.getString("id")}")
//        Timber.i("content_available = ${intent.extras?.getString("content_available")}")
//        Timber.i("intent.extras = ${intent.extras}")


//        intent.extras?.let { bundle ->
//            bundle.getString("title")?.let {
//                openNotificationDialog(bundle)
//            }
//        }

        checkPermission()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    private fun checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED) {
            } else if (shouldShowRequestPermissionRationale(POST_NOTIFICATIONS)) {
                val modalBottomSheet = PermissionRationale(this)
                modalBottomSheet.show(supportFragmentManager, "modalBottomSheet")
            } else {
                requestPermissionLauncher.launch(POST_NOTIFICATIONS)
            }
        }
    }

    private fun openNotificationDialog(bundle: Bundle?){
        navController.navigate(R.id.action_global_notificationDialog, bundle)
    }


    override fun onClickOk() {
        requestPermissionLauncher.launch(POST_NOTIFICATIONS)
    }

}
