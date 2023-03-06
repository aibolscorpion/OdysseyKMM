package kz.divtech.odyssey.rotation.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.databinding.FragmentMainBinding
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.Notification
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.ui.MainActivity
import kz.divtech.odyssey.rotation.ui.profile.notification.NotificationAdapter
import kz.divtech.odyssey.rotation.ui.profile.notification.paging.NotificationListener
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.SegmentAdapter
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.paging.TripsPagingAdapter
import kz.divtech.odyssey.rotation.utils.Utils.appendWithoutNull
import org.threeten.bp.YearMonth
import org.threeten.bp.temporal.WeekFields
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*


class MainFragment : Fragment(), NotificationListener, TripsPagingAdapter.OnTripListener{
    val viewModel : MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory(
            (activity as MainActivity).tripsRepository,
            (activity as MainActivity).employeeRepository,
            (activity as MainActivity).notificationRepository,
            (activity as MainActivity).orgInfoRepository)
    }
    private var _binding: FragmentMainBinding? = null
    val binding get() = _binding!!
    private var nearestTrip : Trip? = null

    private val currentDate: LocalDate = LocalDate.now()
    val dayOfWeek: String = currentDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val dayOfMonth = currentDate.dayOfMonth.toString()
    val month: String = currentDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())

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
        getEmployeeInfo()
        setCalendar()
        setNotifications()
        setNearestTrip()

    }

    private fun getEmployeeInfo(){
        viewModel.getEmployeeFromServer()

        viewModel.employeeLiveData.observe(viewLifecycleOwner){ employee ->
            employee?.let { it ->
                binding.employeeNameTV.text = StringBuilder().appendWithoutNull(it.lastName).
                append(Constants.SPACE).appendWithoutNull(it.firstName).append(Constants.SPACE).
                appendWithoutNull(it.patronymic)
                binding.employeeOrgTV.text = it.orgName
            }
        }
    }

    private fun setCalendar(){
        binding.calendarView.dayBinder = DayAdapter()
        val currentMonth = YearMonth.now()
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        binding.calendarView.setup(currentMonth, currentMonth, firstDayOfWeek)
        binding.calendarView.scrollToMonth(currentMonth)
    }

    private fun setNearestTrip(){
        val segmentAdapter = SegmentAdapter()
        binding.nearestTripView.segmentsRV.adapter = segmentAdapter

        viewModel.getTripsFromFirstPage()
        
        viewModel.nearestActiveTrip.observe(viewLifecycleOwner) { trip ->
            if(trip != null){
                binding.nearestTripTV.isVisible = true
                binding.nearestTripView.root.isVisible = true
                nearestTrip = trip
                binding.trip = nearestTrip
                segmentAdapter.setSegmentList(trip.segments)
            }else{
                binding.nearestTripTV.isVisible = false
                binding.nearestTripView.root.isVisible = false
            }
        }
    }



    private fun setNotifications(){
        val adapter = NotificationAdapter(this)
        binding.notificationsRV.adapter = adapter

        viewModel.getNotificationFromFirstPage()

        viewModel.threeNotifications.observe(viewLifecycleOwner) { notificationList ->

            binding.showAllNotificationsBtn.isVisible = notificationList.isNotEmpty()
            binding.emptyNotificationsTV.isVisible = notificationList.isEmpty()

            notificationList.isNotEmpty().let {
                adapter.setNotificationList(notificationList)
            }

        }
    }

    override fun onNotificationClicked(notification: Notification) {
        findNavController().navigate(
            MainFragmentDirections.actionGlobalNotificationDialog(
                notification.convertToPushNotification())
            )
    }

    override fun onTripClicked(trip: Trip) {
        trip.let {
            if(trip.segments == null){
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

    private fun openVoucherDialog() = findNavController().navigate(
        MainFragmentDirections.actionMainFragmentToVoucherDialog())

    private fun openDebtDialog() = findNavController().navigate(
        MainFragmentDirections.actionMainFragmentToDebtDialog())

    fun openNotificationsFragment() = findNavController().navigate(R.id.action_global_notificationFragment)

    fun showCallSupportDialog() = findNavController().navigate(R.id.action_global_callSupportDialog)

    fun showWriteSupportDialog() = findNavController().navigate(R.id.action_global_writeSupportDialog)

    fun openQuestionsAnswersFragment() = findNavController().navigate(R.id.action_global_questionsAnswersFragment)

}