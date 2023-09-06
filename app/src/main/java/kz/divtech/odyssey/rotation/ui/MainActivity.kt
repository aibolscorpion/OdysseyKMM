package kz.divtech.odyssey.rotation.ui

import android.Manifest
import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Config.UPDATE_TYPE
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
import kz.divtech.odyssey.rotation.utils.SharedPrefs.fetchAppLanguage
import kz.divtech.odyssey.rotation.utils.Utils.changeAppLocale
import kz.divtech.odyssey.rotation.utils.Utils.convertToNotification
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.time.Duration.Companion.seconds


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
    val termsRepository by lazy { TermsRepository(database.dao()) }

    private val viewModel: LogoutViewModel by viewModels{
        LogoutViewModel.LogoutViewModelFactory(tripsRepository, employeeRepository,
            faqRepository, newsRepository, articleRepository,
            notificationRepository, orgInfoRepository, termsRepository)
    }
    private var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!

    private lateinit var appUpdateManager: AppUpdateManager
    private val updateRequestCode = 123

    private val connectivityManager: ConnectivityManager by lazy{
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) {}

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    companion object {
        private const val REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1001
    }

    @OptIn(NavigationUiSaveStateControl::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        if(UPDATE_TYPE == AppUpdateType.FLEXIBLE){
            appUpdateManager.registerListener(installStateUpdatedListener)
        }
        checkForAppUpdates()

        viewModel.uaConfirmedLiveData.observe(this){ isUAConfirmed ->
            isUAConfirmed?.let {
                if(it.not()){
                    openTermsOfAgreementFragment()
                }
            }
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
                R.id.smsCodeFragment, R.id.termsOfAgreementFragment,
                R.id.changeLanguageFragment ->
                    binding.mainToolbar.setNavigationIcon(R.drawable.icons_tabs_back)
                R.id.refundSentFragment, R.id.phoneUpdatedFragment ->
                    binding.mainToolbar.navigationIcon = null
            }
        }
        checkPermission()
        ifPushNotificationSent(intent, false)

        networkCallback = object: ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: Network) {
                runOnUiThread {
                    binding.noInternetLL.isVisible = false
                }
            }

            override fun onLost(network: Network) {
                runOnUiThread{
                    binding.noInternetLL.isVisible = true
                }
            }
        }
        connectivityManager.registerDefaultNetworkCallback(networkCallback as ConnectivityManager.NetworkCallback)
        firebaseAnalytics = Firebase.analytics

        checkWriteExternalStoragePermission()
    }
    override fun attachBaseContext(newBase: Context?) {
        val context = newBase?.changeAppLocale(App.appContext.fetchAppLanguage())
        super.attachBaseContext(context)
    }

    override fun onStart() {
        super.onStart()

        EventBus.getDefault().register(this)
    }

    override fun onResume() {
        super.onResume()

        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
                if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    if(UPDATE_TYPE == AppUpdateType.IMMEDIATE){
                        appUpdateManager.startUpdateFlowForResult(info, this,
                            AppUpdateOptions.newBuilder(UPDATE_TYPE).build(), updateRequestCode)
                    }
                }else if (info.installStatus() == InstallStatus.DOWNLOADED){
                    showToastForCompleteUpdate()
                }
            }
    }

    private fun checkWriteExternalStoragePermission(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_WRITE_EXTERNAL_STORAGE)
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.not_granted_write_external_storage_permission,
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        ifPushNotificationSent(intent, true)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private val installStateUpdatedListener = InstallStateUpdatedListener{ state ->
        if(state.installStatus() == InstallStatus.DOWNLOADED){
            showToastForCompleteUpdate()
        }
    }
    private fun showToastForCompleteUpdate(){
        Toast.makeText(this, R.string.download_successful_restart_in_5_seconds,
            Toast.LENGTH_SHORT).show()
        lifecycleScope.launch {
            delay(5.seconds)
            appUpdateManager.completeUpdate()
        }
    }
    private fun checkForAppUpdates(){
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            val isUpdateAvailable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isUpdateAllowed = when(UPDATE_TYPE){
                AppUpdateType.FLEXIBLE -> info.isFlexibleUpdateAllowed
                AppUpdateType.IMMEDIATE -> info.isImmediateUpdateAllowed
                else -> false
            }
            if(isUpdateAvailable && isUpdateAllowed){
                appUpdateManager.startUpdateFlowForResult(info, this,
                    AppUpdateOptions.newBuilder(UPDATE_TYPE).build(), updateRequestCode)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == updateRequestCode){
            if(resultCode != RESULT_OK){
                if(UPDATE_TYPE == AppUpdateType.IMMEDIATE){
                    checkForAppUpdates()
                }
            }
        }
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

    private fun openTermsOfAgreementFragment(){
        navController.navigate(MainActivityDirections.actionGlobalAuthTermsFragment())
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

        if(UPDATE_TYPE == AppUpdateType.FLEXIBLE){
            appUpdateManager.unregisterListener(installStateUpdatedListener)
        }
        _binding = null

        networkCallback?.let {
            connectivityManager.unregisterNetworkCallback(it)
        }
    }

}
