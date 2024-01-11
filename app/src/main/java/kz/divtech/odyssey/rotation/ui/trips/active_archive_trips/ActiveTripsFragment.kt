package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.common.Constants
import kz.divtech.odyssey.rotation.databinding.FragmentActiveTripsBinding
import kz.divtech.odyssey.rotation.domain.model.EmptyData
import kz.divtech.odyssey.rotation.ui.profile.notification.paging.LoaderAdapter
import kz.divtech.odyssey.rotation.ui.trips.TripsFragmentDirections
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.FilterTripDialog
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.OnFilterClicked
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.SortTripDialog
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.SortTripType.BY_STATUS
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.SortTripType.BY_DEPARTURE_DATE
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.paging.TripsPagingAdapter
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Trip
import java.net.UnknownHostException

@AndroidEntryPoint
class ActiveTripsFragment : Fragment(), TripsPagingAdapter.OnTripListener, LoaderAdapter.RetryCallback,
    SortTripDialog.OnTripSortClicked, OnFilterClicked {
    val refreshing = ObservableBoolean()
    val adapter: TripsPagingAdapter by lazy { TripsPagingAdapter(this) }
    val viewModel: ActiveTripsViewModel by viewModels()
    private var _binding: FragmentActiveTripsBinding? = null
    private val binding get() = _binding!!
    private var isActiveTrips: Boolean? = null

    companion object {
        fun newInstance(activeTrips: Boolean) =
            ActiveTripsFragment().apply{
                arguments  = bundleOf(Constants.ACTIVE_TRIPS to activeTrips)
            }
    }

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getTrips()
            } else {
                Toast.makeText(
                    requireContext(),
                    R.string.not_granted_write_external_storage_permission,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentActiveTripsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.thisFragment = this
        setupTripsPagingAdapter()
        loadStates()

        viewModel.sortType.observe(viewLifecycleOwner){ type ->
            when(type){
                BY_STATUS -> binding.sortTripTV.text = getString(R.string.sort_by_status)
                else -> binding.sortTripTV.text = getString(R.string.sort_by_departure_date)
            }
        }

        viewModel.appliedFilterCount.observe(viewLifecycleOwner) { appliedFilterCount ->
            binding.appliedFilterCountTV.isVisible = appliedFilterCount != 0
            binding.appliedFilterCountTV.text = appliedFilterCount.toString()
        }
    }

    private fun setupTripsPagingAdapter(){
        binding.tripsRV.adapter = adapter.withLoadStateFooter(LoaderAdapter(this))
        isActiveTrips = arguments?.getBoolean(Constants.ACTIVE_TRIPS)
        getTripsIfPermissionIsGranted()
    }

    private fun loadStates(){
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest{ loadState ->

                val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
                val activeTripsEmptyData = EmptyData(ContextCompat.getDrawable(requireContext(), R.drawable.icon_travel)!!,
                        requireContext().getString(R.string.empty_active_trips_title),
                        requireContext().getString(R.string.empty_active_trips_content))

                val archiveTripsEmptyData = EmptyData(ContextCompat.getDrawable(requireContext(), R.drawable.icon_travel)!!,
                    requireContext().getString(R.string.empty_archive_trips_title),
                    requireContext().getString(R.string.empty_archive_trips_content))

                binding.emptyData = if(isActiveTrips!!) activeTripsEmptyData else archiveTripsEmptyData
                binding.emptyTrips.root.isVisible = isListEmpty

                binding.tripsRV.isVisible =
                    loadState.source.refresh is LoadState.NotLoading
                            || loadState.mediator?.refresh is LoadState.NotLoading

                binding.tripsPBar.isVisible = loadState.mediator?.append is LoadState.Loading

                binding.tripsRetryBtn.isVisible = loadState.mediator?.refresh is LoadState.Error
                        && adapter.itemCount == 0

                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error

                errorState?.let {
                    if(errorState.error !is UnknownHostException){
                        Toast.makeText(requireContext(), errorState.error.toString(),
                            Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    private fun getTrips(){
        refreshing.set(true)
        lifecycleScope.launch{
            adapter.submitData(PagingData.empty())
            isActiveTrips?.let { viewModel.getFlowTrips(it).collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }}
        }
        refreshing.set(false)
    }


    fun getTripsIfPermissionIsGranted(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            if (ContextCompat.checkSelfPermission(requireContext(), WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(WRITE_EXTERNAL_STORAGE)
            }else{
                getTrips()
            }
        }else{
            getTrips()
        }
    }

    fun openSortTripDialog(){
        val sortTripDialog = viewModel.sortType.value?.let { SortTripDialog(this, it) }
        sortTripDialog?.show(activity?.supportFragmentManager!!, "SortTripDialog")
    }

    fun openFilterTripDialog(){
        val filterTripDialog = FilterTripDialog(this)
        filterTripDialog.show(childFragmentManager, "FilterTripDialog")
    }

    override fun onTripClicked(trip: Trip?) {
        trip?.let {
            with(findNavController()){
                if(R.id.tripsFragment == currentDestination?.id){
                    if(trip.segments.isEmpty()){
                        navigate(TripsFragmentDirections.actionGlobalTicketsAreNotPurchasedDialog(trip))
                    }else {
                        navigate(TripsFragmentDirections.actionGlobalTripDetailDialog(trip))
                    }
                }
            }
        }
    }

    init {
        this.arguments = Bundle()
    }

    override fun onRetryClicked() {
        adapter.retry()
    }

    override fun onDateClicked() {
        viewModel.setSortType(BY_DEPARTURE_DATE)
        getTripsIfPermissionIsGranted()
    }

    override fun onStatusClicked() {
        viewModel.setSortType(BY_STATUS)
        getTripsIfPermissionIsGranted()
    }

    override fun applyFilterClicked() {
        getTripsIfPermissionIsGranted()
    }

}