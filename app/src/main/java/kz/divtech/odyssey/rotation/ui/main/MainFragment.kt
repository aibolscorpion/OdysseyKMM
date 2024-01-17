package kz.divtech.odyssey.rotation.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentMainBinding
import kz.divtech.odyssey.rotation.ui.profile.notification.NotificationAdapter
import kz.divtech.odyssey.rotation.ui.profile.notification.paging.NotificationListener
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.SegmentAdapter
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.paging.TripsPagingAdapter
import kz.divtech.odyssey.rotation.common.utils.Utils.changeStatusBarColor
import kz.divtech.odyssey.rotation.common.utils.Utils.getAppLocale
import kz.divtech.odyssey.rotation.common.utils.Utils.setMainActivityBackgroundColor
import kz.divtech.odyssey.rotation.common.utils.Utils.showBottomNavigation
import kz.divtech.odyssey.rotation.common.utils.Utils.showToolbar
import kz.divtech.odyssey.shared.data.local.data_store.DataStoreManager
import kz.divtech.odyssey.shared.domain.model.profile.notifications.Notification
import kz.divtech.odyssey.shared.domain.model.profile.notifications.convertToPushNotification
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Trip
import org.threeten.bp.YearMonth
import org.threeten.bp.temporal.WeekFields
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(), NotificationListener, TripsPagingAdapter.OnTripListener{
    val viewModel : MainViewModel by viewModels()
    private var _binding: FragmentMainBinding? = null
    val binding get() = _binding!!

    @Inject
    lateinit var dataStoreManager: DataStoreManager


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater)
        binding.mainFragment = this
        binding.viewModel = viewModel
        binding.listener = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.sendDeviceInfo()
        viewModel.getOrgInfoFromServer()

        binding.voucherCL.setOnClickListener { openVoucherDialog() }
        binding.debtCL.setOnClickListener { openDebtDialog() }

        showBottomNavigation()
        showToolbar()
        changeStatusBarColor(R.color.toolbar_bg)
        setMainActivityBackgroundColor(R.color.main_bg)

        getEmployeeInfo()
        setCalendar()
        setNotifications()
        setNearestTrip()
    }

    override fun onResume() {
        super.onResume()

        val bundle = bundleOf()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, getString(R.string.main))
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "MainFragment")
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    private fun getEmployeeInfo(){
        viewModel.employeeLiveData.observe(viewLifecycleOwner){ employee ->
            employee?.let {
                binding.employeeNameTV.text = it.fullName
            }
        }
        lifecycleScope.launch {
            val organizationName = dataStoreManager.getOrganizationName().first()
            binding.employeeOrgTV.text = organizationName
        }

    }

    private fun setCalendar(){
        val currentDate: LocalDate = LocalDate.now()
        val dayOfWeek: String = currentDate.dayOfWeek.getDisplayName(TextStyle.FULL, requireContext().getAppLocale())
        binding.dayOfWeekTV.text = dayOfWeek
        val dayOfMonth = currentDate.dayOfMonth.toString()
        binding.dayOfMonthTV.text = dayOfMonth
        val month: String = currentDate.month.getDisplayName(TextStyle.FULL, requireContext().getAppLocale())
        binding.monthTV.text = month

        binding.calendarView.dayBinder = DayAdapter()
        val currentMonth = YearMonth.now()
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        binding.calendarView.setup(currentMonth, currentMonth, firstDayOfWeek)
        binding.calendarView.scrollToMonth(currentMonth)
    }

    private fun setNearestTrip(){
        viewModel.getNearestActiveTrip()

        val segmentAdapter = SegmentAdapter()
        binding.nearestTripView.segmentsRV.adapter = segmentAdapter

        lifecycleScope.launch {
            viewModel.getNearestActiveTripFromDb().observe(viewLifecycleOwner) { trip ->
                val isTripAvailable = (trip != null)
                binding.nearestTripTV.isVisible = isTripAvailable
                binding.nearestTripView.root.isVisible = isTripAvailable

                trip?.let{ data ->
                    binding.trip = data
                    segmentAdapter.setSegmentList(data)
                }
            }
        }
    }



    private fun setNotifications(){
        val adapter = NotificationAdapter(this)
        binding.notificationsRV.adapter = adapter

        viewModel.getNotificationFromFirstPage()

        lifecycleScope.launch {
            viewModel.getThreeNotificationsFromDB().observe(viewLifecycleOwner) { notificationList ->
                binding.showAllNotificationsBtn.isVisible = notificationList.isNotEmpty()
                binding.emptyNotificationsTV.isVisible = notificationList.isEmpty()

                notificationList.isNotEmpty().let {
                    adapter.setNotificationList(notificationList)
                }
            }
        }
    }

    override fun onNotificationClicked(notification: Notification) {
        findNavController().navigate(
            MainFragmentDirections.actionGlobalNotificationDialog(
                notification.convertToPushNotification())
            )
    }

    override fun onTripClicked(trip: Trip?) {
        trip?.let {
            if(trip.segments.isEmpty()){
                findNavController().navigate(MainFragmentDirections.actionGlobalTicketsAreNotPurchasedDialog(trip))
            }else {
                findNavController().navigate(MainFragmentDirections.actionGlobalTripDetailDialog(trip))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun openVoucherDialog() {
        with(findNavController()){
            if(R.id.mainFragment == currentDestination?.id){
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToVoucherDialog())
            }
        }
    }

    private fun openDebtDialog() {
        with(findNavController()){
            if(R.id.mainFragment == currentDestination?.id){
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToDebtDialog())
            }
        }
    }

    fun openNotificationsFragment() = findNavController().navigate(R.id.action_global_notificationFragment)

    fun showCallSupportDialog() = findNavController().navigate(R.id.action_global_callSupportDialog)

    fun showWriteSupportDialog() = findNavController().navigate(R.id.action_global_writeSupportDialog)

    fun openQuestionsAnswersFragment() = findNavController().navigate(R.id.action_global_questionsAnswersFragment)

}