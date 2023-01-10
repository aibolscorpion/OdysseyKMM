package kz.divtech.odyssey.rotation.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.databinding.FragmentMainBinding
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.ui.profile.notification.NotificationAdapter
import kz.divtech.odyssey.rotation.ui.profile.notification.NotificationList
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.adapters.SegmentAdapter
import kz.divtech.odyssey.rotation.utils.RoundedCornersTransformation
import kz.divtech.odyssey.rotation.utils.Utils.appendWithoutNull
import org.threeten.bp.YearMonth
import org.threeten.bp.temporal.WeekFields
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*


class MainFragment : Fragment(){
    val viewModel : MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((requireActivity().application as App).tripsRepository,
            (requireActivity().application as App).employeeRepository)
    }
    lateinit var binding : FragmentMainBinding
    private var nearestTrip : Trip? = null

    private val currentDate: LocalDate = LocalDate.now()
    val dayOfWeek: String = currentDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val dayOfMonth = currentDate.dayOfMonth.toString()
    val month: String = currentDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater)
        binding.mainFragment = this
        binding.viewModel = viewModel

        Glide.with(this).load(R.mipmap.avatar_placeholder).apply(
                RequestOptions.bitmapTransform(
                    RoundedCornersTransformation(requireContext(), 18, 2, "#15748595", 10)
                )).into(binding.avatarIV)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getEmployeeInfo()
        setCalendar()
        setNotifications()
        setNearestTrip()
    }

    private fun getEmployeeInfo(){
        viewModel.employeeLiveData.observe(viewLifecycleOwner){ employee ->
            if(employee != null){
                binding.employeeNameTV.text = StringBuilder().appendWithoutNull(employee.lastName).
                append(Constants.SPACE).appendWithoutNull(employee.firstName).append(Constants.SPACE).
                appendWithoutNull(employee.patronymic)
                binding.employeeOrgTV.text = employee.orgName
            }else{
                viewModel.getEmployeeFromServer()
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

    private fun setNotifications(){
        binding.notificationsRV.adapter = NotificationAdapter(NotificationList.getList().subList(0,3))
    }

    private fun setNearestTrip(){
        val segmentAdapter = SegmentAdapter()
        binding.nearestTrip.segmentsRV.adapter = segmentAdapter
        binding.nearestTrip.touchOverlay.setOnClickListener{ onTripClicked(nearestTrip) }

        viewModel.tripsLiveData.observe(viewLifecycleOwner){ data ->
            if(data == null){
                viewModel.getTripsFromServer()
            }else{
                viewModel.findNearestTrip(data.data.data!!)
            }
        }

        viewModel.nearestTripLiveData.observe(viewLifecycleOwner) { trip ->
            nearestTrip = trip
            binding.trip = nearestTrip
            segmentAdapter.setSegmentList(trip?.segments)
        }
    }

    fun onTripClicked(trip: Trip?) {
        if (trip != null) {
            if(trip.segments == null){
                findNavController().navigate(MainFragmentDirections.actionGlobalTicketsAreNotPurchasedDialog(trip))
            }else {
                findNavController().navigate(MainFragmentDirections.actionGlobalTripDetailDialog(trip))
            }
        }
    }

    fun openNotificationsFragment() = findNavController().navigate(R.id.action_global_notificationFragment)

    fun showCallSupportDialog() = findNavController().navigate(R.id.action_global_callSupportDialog)

    fun showWriteSupportDialog() = findNavController().navigate(R.id.action_global_writeSupportDialog)

    fun openQuestionsAnswersFragment() = findNavController().navigate(R.id.action_global_questionsAnswersFragment)


}