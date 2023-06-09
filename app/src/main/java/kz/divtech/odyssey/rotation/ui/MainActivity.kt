package kz.divtech.odyssey.rotation.ui

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.gms.auth.api.phone.SmsRetriever
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.Constants.NOTIFICATION_DATA_TITLE
import kz.divtech.odyssey.rotation.app.Constants.NOTIFICATION_TYPE_APPLICATION
import kz.divtech.odyssey.rotation.app.Constants.NOTIFICATION_TYPE_DEVICE
import kz.divtech.odyssey.rotation.app.Constants.NOTIFICATION_TYPE_PHONE
import kz.divtech.odyssey.rotation.app.Constants.NOTIFICATION_TYPE_TICKET
import kz.divtech.odyssey.rotation.data.local.AppDatabase
import kz.divtech.odyssey.rotation.data.remote.retrofit.UnauthorizedEvent
import kz.divtech.odyssey.rotation.databinding.ActivityMainBinding
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.PushNotification
import kz.divtech.odyssey.rotation.domain.repository.*
import kz.divtech.odyssey.rotation.ui.profile.LogoutViewModel
import kz.divtech.odyssey.rotation.ui.profile.notification.push_notification.NotificationListener
import kz.divtech.odyssey.rotation.ui.profile.notification.push_notification.PermissionRationale
import kz.divtech.odyssey.rotation.utils.InputUtils
import kz.divtech.odyssey.rotation.utils.SharedPrefs
import kz.divtech.odyssey.rotation.utils.Utils.convertToNotification
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : AppCompatActivity(), NotificationListener {
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.mainNavHostFragment)
                as NavHostFragment).navController }
    private val database by lazy { AppDatabase.getDatabase(applicationContext) }
    val tripsRepository by lazy { TripsRepository(database.dao()) }
    val employeeRepository by lazy { EmployeeRepository(database.dao()) }
    val faqRepository by lazy { FaqRepository(database.dao()) }
    val newsRepository by lazy { NewsRepository(database.dao()) }
    val articleRepository by lazy { ArticleRepository(database.dao()) }
    val notificationRepository by lazy { NotificationRepository(database.dao()) }
    val orgInfoRepository by lazy { OrgInfoRepository(database.dao()) }
    val refundRepository by lazy { RefundRepository(database.dao()) }
    private val viewModel: LogoutViewModel by viewModels{
        LogoutViewModel.LogoutViewModelFactory(tripsRepository, employeeRepository,
            faqRepository, newsRepository, articleRepository,
            notificationRepository, orgInfoRepository)
    }
    private var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) {}

    @OptIn(NavigationUiSaveStateControl::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if(SharedPrefs.isLoggedIn(this)){
            openMainFragment()
        }

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController, false)

        setSupportActionBar(binding.mainToolbar)
        setupActionBarWithNavController(navController, AppBarConfiguration(setOf(R.id.mainFragment,
            R.id.tripsFragment, R.id.searchFragment, R.id.helpFragment, R.id.profileFragment)))

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.personalDataFragment, R.id.documentsFragment,
                R.id.notificationFragment, R.id.faqFragment,
                R.id.newsFragment, R.id.chooseTicketRefundFragment,
                R.id.refundReasonFragment, R.id.refundListFragment,
                R.id.refundDetailFragment, R.id.chooseTicketForOpen,
                R.id.countryListFragment, R.id.phoneNumberFragment2,
                R.id.smsCodeFragment ->
                    binding.mainToolbar.setNavigationIcon(R.drawable.icons_tabs_back)
                R.id.refundSentFragment, R.id.phoneUpdatedFragment ->
                    binding.mainToolbar.navigationIcon = null
            }
        }
        checkPermission()
        ifPushNotificationSent(intent, false)

        SmsRetriever.getClient(this).startSmsRetriever().addOnFailureListener { exception ->
            exception.message?.let { InputUtils.showErrorMessage(this, binding.root, it) }
        }

    }

    override fun onStart() {
        super.onStart()

        EventBus.getDefault().register(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        ifPushNotificationSent(intent, true)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

//    fun checkUpdate(){
//        val appUpdateManager = AppUpdateManagerFactory.create(this)
//        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
//
//        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
//            ) {
//                // Request the update.
//            }
//        }
//    }
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

    private fun ifPushNotificationSent(intent: Intent?, onNewIntent : Boolean) {
        intent?.extras?.let { bundle ->
            bundle.getString(NOTIFICATION_DATA_TITLE)?.let{
                val notification = bundle.convertToNotification()
                when(notification.type){
                    NOTIFICATION_TYPE_PHONE -> showPhoneNumberChangedDialog(intent)
                    NOTIFICATION_TYPE_DEVICE -> openLoggedOutNotificationDialog(notification)
                    NOTIFICATION_TYPE_APPLICATION, NOTIFICATION_TYPE_TICKET -> {
                        if(onNewIntent) {
                            viewModel.getNotificationsFromServer()
                        }
                        openNotificationDialog(notification)
                    }
                    else -> {
                        if(onNewIntent) {
                            viewModel.getNotificationsFromServer()
                        }
                        openNotificationDialog(notification)
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUnauthorizedEvent(e: UnauthorizedEvent){
        Toast.makeText(this, R.string.you_are_unauthorized, Toast.LENGTH_SHORT).show()
        lifecycleScope.launch{
            viewModel.deleteAllDataAsync().await()
            goToLoginPage()
        }
    }


    private fun openNotificationDialog(notification: PushNotification){
        navController.navigate(MainActivityDirections.actionGlobalNotificationDialog(notification))
    }

    private fun openLoggedOutNotificationDialog(notification: PushNotification){
        navController.navigate(MainActivityDirections.actionGlobalLoggedOutNotificationDialog(notification))
    }

    private fun goToLoginPage(){
        navController.navigate(MainActivityDirections.actionGlobalPhoneNumberFragment())
    }

    private fun showPhoneNumberChangedDialog(intent: Intent){
        navController.navigate(R.id.phoneNumberAddedDialog, intent.extras)
    }

    private fun openMainFragment(){
        navController.navigate(MainActivityDirections.actionGlobalMainFragment())
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onClickOk() {
        requestPermissionLauncher.launch(POST_NOTIFICATIONS)
    }

    override fun onStop() {
        super.onStop()

        EventBus.getDefault().unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

}
